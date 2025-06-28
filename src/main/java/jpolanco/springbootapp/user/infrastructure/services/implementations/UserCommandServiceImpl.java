package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.application.uc.base.*;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;
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
    public Report update(AllFieldsUserUpdate request, String userId) {
        var report = updateUser.setChanges(userId, request);
        if (report.isFailure()) return report;
        var domainEvents = report.getNotifications();
        publisher.publishAll(domainEvents);
        return report;
    }

    @Transactional
    @Override
    public Result<Void> deleteById(String userId, String reason) {
        var result = deleteUserUC.deleteById(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Override
    public Result<Void> reactivateById(String userId) {
        var result = reactivateUserUC.reactivateById(userId);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Override
    public Result<Void> deactivateById(String userId, String reason) {
        var result = deactivateUserUC.deactivateById(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Override
    public Result<Void> suspendById(String userId, String reason) {
        var result = suspendUserUC.suspendById(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }
}
