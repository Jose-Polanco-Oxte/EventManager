package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.ReactivateUserUC;
import jpolanco.springbootapp.user.application.uc.derived.ReactivateProfileUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactivateProfile implements ReactivateProfileUC {
    private final UserQueryRepository queryRepository;
    private final ReactivateUserUC reactivateUserUC;

    @Override
    public Result<User> reactivate(String userId) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        if (user.isActive()) {
            return Result.failure(UserAppError.USER_ALREADY_ACTIVE);
        }
        return reactivateUserUC.reactivate(user);
    }
}
