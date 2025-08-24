package jpolanco.applicationcore.user.application.services.implementation.commands;

import jpolanco.applicationcore.audit.utils.Audit;
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
import jpolanco.applicationcore.user.application.events.UserDeleted;
import jpolanco.applicationcore.user.application.ports.output.TokenCommandRepository;
import jpolanco.applicationcore.user.application.services.interfaces.commands.DeleteAccount;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.domain.services.DeletionPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteAccountDefault implements DeleteAccount {

    private final UserRepository userRepository;

    private final TokenCommandRepository tokenCommandRepository;

    private final DeletionPolicyService deletionPolicyService;

    private final FindPairs<User, UUID> findUserPair;

    private final EventBus<EventNotification> publisher;

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public void deleteById(UUID mainAccount, UUID targetAccount, String reason) {
        Pair<User, User> users = findUserPair.find(mainAccount, targetAccount, User.class);

        User mainUser = users.getFirst();
        User targetUser = users.getSecond();

        if (mainUser.equals(targetUser)) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError(
                    "Cannot delete the main user account. Use self-deletion instead."));
        }

        Result<Void, DomainError> policyCheck = deletionPolicyService.check(mainUser, targetUser);

        softDelete(reason, targetUser, policyCheck);
    }

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public void deleteMainById(UUID mainAccount, String reason) {
        Optional<User> maybeMain = userRepository.findById(mainAccount);

        if (maybeMain.isEmpty()) {
            throw new ApplicationExceptionHandler(NotFound.entityNotFound("Main account", mainAccount.toString()));
        }

        User mainUser = maybeMain.get();

        Result<Void, DomainError> policyCheck = deletionPolicyService.checkSelf(mainUser);

        softDelete(reason, mainUser, policyCheck);
    }

    private void softDelete(String reason, User mainUser, Result<Void, DomainError> policyCheck) {
        if (policyCheck.isFailure()) {
            throw new DomainExceptionHandler(policyCheck.getErrors());
        }

        EventNotification event = new UserDeleted(
                mainUser.getUserId().getUUID(),
                reason,
                mainUser.getStatus()
        );

        userRepository.softDelete(mainUser.getUserId().getId());

        tokenCommandRepository.deleteAllByUserId(mainUser.getUserId().getUUID());

        publisher.publish(event);
    }
}
