package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.controllers.ResponseHandler;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.application.uc.derived.*;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * WARNING: This code is under refactoring and now not be functional.
 */

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
    private final ResponseHandler responseHandler;

    @Transactional
    @Override
    public Result<Void> changeEmail(String userId, UpdateEmailRequest request) {
        var maybeUser = updateProfileEmailUC.setEmail(userId, request);
//        if (maybeUser.isFailure()) {
//            return Result.failure(maybeUser.getError());
//        }
//        var user = maybeUser.getValue();
//        publisher.publishAll(user.pullEvents());
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> changeName(String userId, UpdateNameRequest request) {
        var maybeUser = updateProfileNameUC.setName(userId, request);
//        if (maybeUser.isFailure()) {
//            return Result.failure(maybeUser.getError());
//        }
//        var user = maybeUser.getValue();
//        publisher.publishAll(user.pullEvents());
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> delete(String userId, String reason) {
        var result =deleteProfileUC.delete(userId, reason);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var domainEvents = result.getValue();
        publisher.publishAll(domainEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> changePassword(String userId, UpdatePasswordRequest dto) {
        var maybeUser = updateProfilePasswordUC.setPassword(userId, dto);
//        if (maybeUser.isFailure()) {
//            return Result.failure(maybeUser.getError());
//        }
//        var user = maybeUser.getValue();
//        publisher.publishAll(user.pullEvents());
        return Result.success();
    }

    @Override
    public Result<Void> deactivate(String userId, String reason) {
        var maybeUser = deactivateProfileUC.deactivate(userId, reason);
//        if (maybeUser.isFailure()) {
//            return Result.failure(maybeUser.getError());
//        }
//        var user = maybeUser.getValue();
//        publisher.publishAll(user.pullEvents());
        return Result.success();
    }

    @Override
    public Result<Void> reactivate(String userId) {
        var maybeUser = reactivateProfileUC.reactivate(userId);
//        if (maybeUser.isFailure()) {
//            return Result.failure(maybeUser.getError());
//        }
//        var user = maybeUser.getValue();
//        publisher.publishAll(user.pullEvents());
        return Result.success();
    }
}
