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
import jpolanco.applicationcore.user.application.services.interfaces.commands.ReactivateAccount;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.domain.services.ReactivationPolicyService;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactivateAccountDefault implements ReactivateAccount {

    private final UserRepository userRepository;

    private final ReactivationPolicyService reactivationPolicyService;

    private final FindPairs<User, UUID> findUserPair;

    private final EventBus<EventNotification> publisher;

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public UserResponse reactivateById(UUID mainAccount, UUID targetAccount) {
        Pair<User, User> users = findUserPair.find(mainAccount, targetAccount, User.class);

        User mainUser = users.getFirst();
        User targetUser = users.getSecond();

        if (mainUser.equals(targetUser)) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError(
                    "Cannot reactivate the main user account. Use self-reactivation instead."));
        }

        Result<Void, DomainError> policyCheck = reactivationPolicyService.check(mainUser, targetUser);

        return getUserResponse(targetUser, policyCheck);
    }

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public UserResponse reactivateMainById(UUID mainAccount) {
        Optional<User> maybeMain = userRepository.findById(mainAccount);

        if (maybeMain.isEmpty()) {
            throw new ApplicationExceptionHandler(NotFound.entityNotFound("Main user", mainAccount.toString()));
        }

        User mainUser = maybeMain.get();

        Result<Void, DomainError> policyCheck;

        policyCheck = reactivationPolicyService.checkSelf(mainUser);

        return getUserResponse(mainUser, policyCheck);
    }

    @NotNull
    private UserResponse getUserResponse(User mainUser, Result<Void, DomainError> policyCheck) {
        if (policyCheck.isFailure()) {
            throw new DomainExceptionHandler(policyCheck.getErrors());
        }

        mainUser.reactivate();
        User savedMainUser = userRepository.save(mainUser);
        if (savedMainUser == null) {
            throw new ApplicationExceptionHandler(Internal.internalError("Failed to save reactivated user"));
        }

        publisher.publishAll(savedMainUser.pullEvents());

        return UserDto.toResponse(savedMainUser);
    }
}
