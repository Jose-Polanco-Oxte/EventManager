package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.utils.results.reports.UpdateReport;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.application.usecase.base.*;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AllFieldsUserUpdate;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
    private final UpdateUser updateUser;
    private final DeleteUser deleteUser;
    private final ReactivateUser reactivateUser;
    private final DeactivateUser deactivateUser;
    private final SuspendUser suspendUser;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public UpdateReport update(UUID userId, AllFieldsUserUpdate request) {
        var report = updateUser.setChanges(userId, request);
        if (report.isFailure()) return report;
        var domainEvents = report.getNotifications();
        publisher.publishAll(domainEvents);
        return report;
    }

    @Transactional
    @Override
    public Result<Void> deleteById(UUID userId, String reason) {
        var result = deleteUser.deleteById(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> reactivateById(UUID userId) {
        var result = reactivateUser.reactivateById(userId);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> deactivateById(UUID userId, String reason) {
        var result = deactivateUser.deactivateById(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> suspendById(UUID userId, String reason) {
        var result = suspendUser.suspendById(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }
}
