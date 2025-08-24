package jpolanco.applicationcore.user.application.services.implementation.commands;

import jpolanco.applicationcore.audit.utils.Audit;
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
import jpolanco.applicationcore.user.application.ports.output.TokenCommandRepository;
import jpolanco.applicationcore.user.application.services.interfaces.commands.SuspendAccount;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.domain.repositories.UserRepository;
import jpolanco.applicationcore.user.domain.services.SuspensionPolicyService;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuspendAccountDefault implements SuspendAccount {

    private final UserRepository userRepository;

    private final SuspensionPolicyService suspensionPolicyService;

    private final TokenCommandRepository tokenCommandRepository;

    private final FindPairs<User, UUID> findUserPair;

    private final EventBus<EventNotification> publisher;

    @Audit(action = OPERATION)
    @Transactional
    @Override
    public UserResponse suspendById(UUID mainAccount, UUID targetAccount, String reason) {
        Pair<User, User> users = findUserPair.find(mainAccount, targetAccount, User.class);

        User mainUser = users.getFirst();
        User targetUser = users.getSecond();

        if (mainUser.equals(targetUser)) {
            throw new ApplicationExceptionHandler(InvalidOperation.invalidActionError("You cannot suspend your own account"));
        }

        Result<Void, DomainError> policyCheck = suspensionPolicyService.check(mainUser, targetUser);

        if (policyCheck.isFailure()) {
            throw new DomainExceptionHandler(policyCheck.getErrors());
        }

        targetUser.suspend(reason);

        User savedTargetUser = userRepository.save(targetUser);

        publisher.publishAll(savedTargetUser.pullEvents());

        tokenCommandRepository.deleteAllByUserId(savedTargetUser.getUserId().getUUID());

        return UserDto.toResponse(targetUser);
    }
}
