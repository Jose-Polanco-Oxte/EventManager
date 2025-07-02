package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.UpdateReport;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.application.uc.derived.*;
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
    private final UpdateProfileEmailUC updateProfileEmailUC;
    private final UpdateProfilePasswordUC updateProfilePasswordUC;
    private final UpdateProfileNameUC updateProfileNameUC;
    private final DeleteProfileUC deleteProfileUC;
    private final ReactivateProfileUC reactivateProfileUC;
    private final DeactivateProfileUC deactivateProfileUC;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public Result<Void> changeEmail(UUID userId, ChangeEmailRequest request) {
        var result = updateProfileEmailUC.setEmail(userId, request);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public UpdateReport changeName(UUID userId, ChangeNameRequest request) {
        var result = updateProfileNameUC.setName(userId, request);
        if (result.isFailure()) return result;
        var domainEvents = result.getNotifications();
        publisher.publishAll(domainEvents);
        return result;
    }

    @Transactional
    @Override
    public Result<Void> delete(UUID userId, String reason) {
        var result = deleteProfileUC.delete(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> changePassword(UUID userId, ChangePasswordRequest dto) {
        var result = updateProfilePasswordUC.setPassword(userId, dto);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> deactivate(UUID userId, String reason) {
        var result = deactivateProfileUC.deactivate(userId, reason);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> reactivate(UUID userId) {
        var result = reactivateProfileUC.reactivate(userId);
        if (result.isFailure()) return Result.failure(result.getError());
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }
}
