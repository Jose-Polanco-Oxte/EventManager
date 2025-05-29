package jpolanco.springbootapp.user.infrastructure.services;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.input.AuxTokenManager;
import jpolanco.springbootapp.user.application.uc.DeleteUserByIdUC;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.application.uc.UpdateUserUC;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.SimpleResponseCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.UserDtoCreator;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateNameRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final UserDtoCreator userDtoCreator;
    private final SimpleResponseCreator simpleResponseCreator;
    private final DeleteUserByIdUC deleteUserByIdUC;
    private final UpdateUserUC updateUserUC;
    private final UserValidation userValidation;
    private final AuxTokenManager auxTokenManager;

    @Override
    public Result<Dto> get(String userId) {
        var valid = userValidation.basicValid(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var user = valid.getValue();
        if (!user.isActive()) {
            return Result.failure(UserAppError.USER_NOT_ACTIVE);
        }
        return Result.success(userDtoCreator.create(user));
    }

    @Override
    public Result<Dto> changeName(String userId, UpdateNameRequest request) {
        var valid = userValidation.basicValid(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var validUser = valid.getValue();
        var result = updateUserUC.setChanges(validUser)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .update();
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(simpleResponseCreator.create("Name updated successfully"));
    }

    @Override
    public Result<Dto> changeEmail(String userId, UpdateEmailRequest dto) {
        var valid = userValidation.basicValid(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var validUser = valid.getValue();
        var result = updateUserUC.setChanges(validUser)
                .email(dto.email())
                .update();
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var updatedUser = result.getValue();
        auxTokenManager.revokeAllUserTokens(updatedUser.getId());
        return Result.success(simpleResponseCreator.create("Email updated successfully"));
    }

    @Override
    public Result<Dto> deleteMe(String userId) {
        var result = deleteUserByIdUC.delete(userId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(simpleResponseCreator.create("User deleted successfully"));
    }

    @Override
    public Result<Dto> changePassword(String userId, UpdatePasswordRequest dto) {
        var valid = userValidation.onUpdatePasswordIsValid(dto.newPassword(), dto.oldPassword(), userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var validUser = valid.getValue();
        var result = updateUserUC.setChanges(validUser)
                .password(dto.newPassword())
                .update();
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        auxTokenManager.revokeAllUserTokens(validUser.getId());
        return Result.success(simpleResponseCreator.create("Password updated successfully"));
    }
}
