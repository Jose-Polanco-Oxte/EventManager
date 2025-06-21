package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.uc.base.DeleteUserUC;
import jpolanco.springbootapp.user.application.uc.base.UpdateUserUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.AnyUserUpdateRequest;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
    private final UpdateUserUC updateUser;
    private final DeleteUserUC deleteUserUC;

    @Override
    public Result<Void> update(AnyUserUpdateRequest request, String userId) {
        var result = updateUser.setChanges(userId, request);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success();
    }

    @Override
    public Result<Void> deleteById(String userId) {
        var result = deleteUserUC.deleteById(userId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success();
    }
}
