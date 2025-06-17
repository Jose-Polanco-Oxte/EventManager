package jpolanco.springbootapp.user.infrastructure.services;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.SimpleResponseDto;
import jpolanco.springbootapp.user.application.ports.input.AuxTokenManager;
import jpolanco.springbootapp.user.application.uc.DeleteUserByIdUC;
import jpolanco.springbootapp.user.infrastructure.components.UserValidator;
import jpolanco.springbootapp.user.application.uc.UpdateUserUC;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.SimpleResponseCreator;
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
    private final SimpleResponseCreator simpleResponseCreator;
    private final DeleteUserByIdUC deleteUserByIdUC;
    private final UpdateUserUC updateUserUC;
    private final UserValidator userValidator;
    private final AuxTokenManager auxTokenManager;

    @Transactional
    @Override
    public Result<SimpleResponseDto> changeName(String userId, UpdateNameRequest request) {
        var valid = userValidator.collectUser(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var user = valid.getValue();
        var result = updateUserUC.setChanges(user)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .update();
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(simpleResponseCreator.create("Name updated successfully"));
    }

    @Transactional
    @Override
    public Result<SimpleResponseDto> changeEmail(String userId, UpdateEmailRequest request) {
        var valid = userValidator.collectUser(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var user = valid.getValue();
        var result = updateUserUC.setChanges(user)
                .email(request.email())
                .update();
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var updatedUser = result.getValue();
        auxTokenManager.revokeAllUserTokens(updatedUser.getId());
        return Result.success(simpleResponseCreator.create("Email updated successfully"));
    }

    @Transactional
    @Override
    public Result<SimpleResponseDto> deleteMe(String userId) {
        var valid = userValidator.collectUser(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var user = valid.getValue();
        var result = deleteUserByIdUC.delete(user.getId());
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(simpleResponseCreator.create("User deleted successfully"));
    }

    @Transactional
    @Override
    public Result<SimpleResponseDto> changePassword(String userId, UpdatePasswordRequest dto) {
        var valid = userValidator.onUpdatePasswordIsValid(userId, dto.newPassword(), dto.oldPassword());
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var user = valid.getValue();
        var result = updateUserUC.setChanges(user)
                .password(dto.newPassword())
                .update();
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        auxTokenManager.revokeAllUserTokens(user.getId());
        return Result.success(simpleResponseCreator.create("Password updated successfully"));
    }
}
