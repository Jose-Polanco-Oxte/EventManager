package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.application.uc.base.*;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
    private final UpdateUserUC updateUser;
    private final DeleteUserUC deleteUserUC;
    private final ReactivateUserUC reactivateUserUC;
    private final DeactivateUserUC deactivateUserUC;
    private final SuspendUserUC suspendUserUC;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public Result<Void> update(AnyUserUpdateRequest request, String userId) {
        var maybeUser = updateUser.setChanges(userId, request);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        publisher.publishAll(user.pullEvents());
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> deleteById(String userId, String reason) {
        var result = deleteUserUC.deleteById(userId, reason);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Override
    public Result<Void> reactivateById(String userId) {
        var maybeUser = reactivateUserUC.reactivateById(userId);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        publisher.publishAll(user.pullEvents());
        return Result.success();
    }

    @Override
    public Result<Void> deactivateById(String userId, String reason) {
        var maybeUser = deactivateUserUC.deactivateById(userId, reason);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        publisher.publishAll(user.pullEvents());
        return Result.success();
    }

    @Override
    public Result<Void> suspendById(String userId, String reason) {
        var maybeUser = suspendUserUC.suspendById(userId, reason);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        publisher.publishAll(user.pullEvents());
        return Result.success();
    }
}
