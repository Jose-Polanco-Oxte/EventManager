package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.ReactivateUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactivateUser implements ReactivateUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public Result<User> reactivate(User user) {
        user.reactivate();
        var userUpdated = commandRepository.save(user);
        return Result.success(userUpdated);
    }

    @Override
    public Result<User> reactivateById(String userId) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        return reactivate(maybeUser.get());
    }
}
