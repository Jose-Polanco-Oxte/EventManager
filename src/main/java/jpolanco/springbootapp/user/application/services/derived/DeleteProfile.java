package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeleteUserUC;
import jpolanco.springbootapp.user.application.uc.derived.DeleteProfileUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProfile implements DeleteProfileUC {
    private final UserQueryRepository queryRepository;
    private final DeleteUserUC deleteUserUC;

    @Override
    public Result<Void> delete(String userId) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        if (user.isSuspended()) {
            return Result.failure(UserAppError.USER_SUSPENDED);
        }
        var result = deleteUserUC.delete(user);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success();
    }
}
