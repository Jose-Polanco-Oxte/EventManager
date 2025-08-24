package jpolanco.applicationcore.user.application.services.implementation.commands;

import jakarta.validation.constraints.NotNull;
import jpolanco.applicationcore.audit.utils.Audit;
import jpolanco.applicationcore.shared.application.errors.Internal;
import jpolanco.applicationcore.shared.application.errors.InvalidOperation;
import jpolanco.applicationcore.shared.application.errors.NotFound;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.application.exceptions.DomainExceptionHandler;
import jpolanco.applicationcore.shared.application.utils.EventBus;
import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.shared.domain.utils.primitives.utils.Pair;
import jpolanco.applicationcore.user.application.components.FindPairs;
import jpolanco.applicationcore.user.application.mappers.UserDto;
import jpolanco.applicationcore.user.application.ports.output.TokenCommandRepository;
import jpolanco.applicationcore.user.application.services.interfaces.commands.DeactivateAccount;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.domain.services.DeactivationPolicyService;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeactivateAccountDefault implements DeactivateAccount {

    private final UserRepository userRepository;

    private final TokenCommandRepository tokenCommandRepository;

    private final DeactivationPolicyService deactivationPolicyService;
    
    private final FindPairs<User, UUID> findUserPair;

    private final EventBus<EventNotification> publisher;

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public UserResponse deactivateById(UUID mainAccount, UUID targetAccount, String reason) {

        Pair<User, User> users = findUserPair.find(mainAccount, targetAccount, User.class);

        User mainUser = users.getFirst();
        User targetUser = users.getSecond();

        if (mainUser.equals(targetUser)) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError(
                    "Cannot deactivate the main user account. Use self-deactivation instead."));
        }

        Result<Void, DomainError> policyCheck = deactivationPolicyService.check(mainUser, targetUser);

        return getUserResponse(reason, targetUser, policyCheck);
    }

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public UserResponse deactivateMainById(UUID mainAccount, String reason) {
        Optional<User> maybeMain = userRepository.findById(mainAccount);

        if (maybeMain.isEmpty()) {
            throw new ApplicationExceptionHandler(NotFound.entityNotFound("Main user", mainAccount.toString()));
        }

        User mainUser = maybeMain.get();

        Result<Void, DomainError> policyCheck = deactivationPolicyService.checkSelf(mainUser);

        return getUserResponse(reason, mainUser, policyCheck);
    }

    @NotNull
    private UserResponse getUserResponse(String reason, User user, Result<Void, DomainError> policyCheck) {
        if (policyCheck.isFailure()) {
            throw new DomainExceptionHandler(policyCheck.getErrors());
        }

        user.deactivate(reason);

        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            throw new ApplicationExceptionHandler(Internal.internalError("Failed to save deactivated user"));
        }

        tokenCommandRepository.deleteAllByUserId(savedUser.getUserId().getUUID());

        publisher.publishAll(user.pullEvents());

        return UserDto.toResponse(savedUser);
    }
}
