package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.dto.SimpleResponseDto;
import jpolanco.springbootapp.user.application.uc.DeleteUserByIdUC;
import jpolanco.springbootapp.user.application.uc.UpdateUserUC;
import jpolanco.springbootapp.user.domain.model.valueobjects.UserStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
import jpolanco.springbootapp.user.infrastructure.components.interfaces.UserValidation;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UpdateUserUC updateUser;
    private final DeleteUserByIdUC deleteUserByIdUC;
    private final UserValidation userValidation;

    @Override
    public Result<SimpleResponseDto> updateUser(AnyUserUpdateRequest request, String userId) {
        var valid = userValidation.collectUser(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var user = valid.getValue();
        var result = updateUser.setChanges(user)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .roles(request.roles())
                .status(UserStatus.valueOf(request.status()))
                .update();
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(new SimpleResponseDto("User updated successfully"));
    }

    @Override
    public Result<SimpleResponseDto> deleteUserById(String userId) {
        var valid = userValidation.idIsValid(userId);
        if (valid.isFailure()) {
            return Result.failure(valid.getError());
        }
        var result = deleteUserByIdUC.delete(userId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(new SimpleResponseDto("User deleted successfully"));
    }
}
