package jpolanco.applicationcore.user.application.services.implementation.commands;

import jpolanco.applicationcore.audit.utils.Audit;
import jpolanco.applicationcore.shared.application.errors.Internal;
import jpolanco.applicationcore.shared.application.errors.InvalidOperation;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.application.exceptions.DomainExceptionHandler;
import jpolanco.applicationcore.shared.application.utils.EventBus;
import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.shared.domain.utils.primitives.utils.Pair;
import jpolanco.applicationcore.user.application.components.FindPairs;
import jpolanco.applicationcore.user.application.mappers.UserDto;
import jpolanco.applicationcore.user.application.ports.input.EncoderProvider;
import jpolanco.applicationcore.user.application.services.interfaces.commands.UpdateAccount;
import jpolanco.applicationcore.user.domain.dto.UpdateAllUser;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.model.UserStatus;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.domain.services.PasswordStrongestPolicyService;
import jpolanco.applicationcore.user.domain.services.UserUpdatePolicyService;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class UpdateAccountDefault implements UpdateAccount {

    private final UserRepository userRepository;

    private final EncoderProvider encoderProvider;

    private final PasswordStrongestPolicyService passwordStrongestPolicyService;

    private final UserUpdatePolicyService userUpdatePolicyService;

    private final FindPairs<User, UUID> findUserPair;

    private final EventBus<EventNotification> publisher;

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public UserResponse setChanges(UUID mainAccount, UUID targetAccount, UpdateAllUser request) {
        Pair<User, User> users = findUserPair.find(mainAccount, targetAccount, User.class);

        User mainUser = users.getFirst();
        User targetUser = users.getSecond();

        if (mainUser.equals(targetUser)) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("You cannot update all your own account, use account service instead."));
        }

        Result<Void, DomainError> checkPolicyUpdate = userUpdatePolicyService.checkAbleToUpdate(mainUser);
        if (checkPolicyUpdate.isFailure()) {
            throw new DomainExceptionHandler(checkPolicyUpdate.getErrors());
        }

        Result<Void, DomainError> checkPasswordStrongest = passwordStrongestPolicyService.check(request.password());


        List<DomainError> errors = new ArrayList<>();

        if (checkPasswordStrongest.isFailure()) {
            errors.addAll(checkPasswordStrongest.getErrors());
        }

        String encodedPassword = encoderProvider.encode(request.password());

        Result<Void, DomainError> result = targetUser.changeFirstName(request.firstName())
                .andThen(r -> targetUser.changeLastName(request.lastName()))
                .andThen(r -> targetUser.changeEmail(request.email()))
                .andThen(r -> targetUser.changeEncodedPassword(encodedPassword));

        if (result.isFailure()) {
            errors.addAll(result.getErrors());
        }

        UserStatus newStatus = UserStatus.fromString(request.status());

        Result<Void, DomainError> checkStatusUpdatePolicy = userUpdatePolicyService
                .checkChangeStatus(
                        mainUser,
                        targetUser,
                        newStatus
                );

        if (checkStatusUpdatePolicy.isFailure()) {
            errors.addAll(checkStatusUpdatePolicy.getErrors());
        } else {
            targetUser.changeStatus(newStatus, "User updated by administrator");
        }

        // Roles management
        if (thereAreRolesToChange(request.roles().add(), request.roles().remove())) { // If there are roles to add or remove
            if (!isEmpty(request.roles().remove())) { // If there are roles to remove
                handleRoles(request.roles().remove(), errors, targetUser::removeRoles);
            }
            if (!isEmpty(request.roles().add())) { // If there are roles to add
                handleRoles(request.roles().add(), errors, targetUser::addRoles);
            }
        }

        if (!errors.isEmpty()) {
            throw new DomainExceptionHandler(errors);
        }

        User savedTargetUser = userRepository.save(targetUser);

        if (savedTargetUser == null) {
            throw new ApplicationExceptionHandler(Internal.internalError("Failed to save updated user"));
        }

        publisher.publishAll(savedTargetUser.pullEvents());

        return UserDto.toResponse(savedTargetUser);
    }

    private boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    private boolean thereAreRolesToChange(List<String> add, List<String> remove) {
        return !(isEmpty(add) && isEmpty(remove));
    }

    private void handleRoles(List<String> toOperate, List<DomainError> errors, Function<List<String>, Result<Void, DomainError>> roleOperation) {
        if (!isEmpty(toOperate)) { // If there are roles to add
            Result<Void, DomainError> result = roleOperation.apply(toOperate); // Apply the operation (add / remove)
            if (result.isFailure()) errors.addAll(result.getErrors()); // Collect errors if any
        }
    }
}