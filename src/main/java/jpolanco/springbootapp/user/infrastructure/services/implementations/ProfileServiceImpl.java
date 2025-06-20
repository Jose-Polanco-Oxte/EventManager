package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.uc.derived.DeleteProfileUC;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfileEmailUC;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfileNameUC;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfilePasswordUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final UpdateProfileEmailUC updateProfileEmailUC;
    private final UpdateProfilePasswordUC updateProfilePasswordUC;
    private final UpdateProfileNameUC updateProfileNameUC;
    private final DeleteProfileUC deleteProfileUC;

    @Transactional
    @Override
    public Result<Void> changeEmail(String userId, UpdateEmailRequest request) {
        var maybeUser = updateProfileEmailUC.setEmail(userId, request);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        return Result.success();
    }

    @Override
    public Result<Void> changeName(String userId, UpdateNameRequest request) {
        var maybeUser = updateProfileNameUC.setName(userId, request);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> delete(String userId) {
        var result =deleteProfileUC.delete(userId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> changePassword(String userId, UpdatePasswordRequest dto) {
        var maybeUser = updateProfilePasswordUC.setPassword(userId, dto);
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        return Result.success();
    }
}
