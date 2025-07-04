package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.utils.results.reports.UpdateReport;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.application.usecase.derived.*;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UpdateAccountEmail updateAccountEmail;
    private final UpdateAccountPassword updateAccountPassword;
    private final UpdateAccountName updateAccountName;
    private final DeleteAccount deleteAccount;
    private final ReactivateAccount reactivateAccount;
    private final DeactivateAccount deactivateAccount;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public Result<Void> changeEmail(UUID userId, ChangeEmailRequest request) {
        var result = updateAccountEmail.setEmail(userId, request);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public UpdateReport changeName(UUID userId, ChangeNameRequest request) {
        var result = updateAccountName.setName(userId, request);
        if (result.isFailure()) return result;
        var domainEvents = result.getNotifications();
        publisher.publishAll(domainEvents);
        return result;
    }

    @Transactional
    @Override
    public Result<Void> delete(UUID userId, String reason) {
        var result = deleteAccount.delete(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> changePassword(UUID userId, ChangePasswordRequest dto) {
        var result = updateAccountPassword.setPassword(userId, dto);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> deactivate(UUID userId, String reason) {
        var result = deactivateAccount.deactivate(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> reactivate(UUID userId) {
        var result = reactivateAccount.reactivate(userId);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }
}
