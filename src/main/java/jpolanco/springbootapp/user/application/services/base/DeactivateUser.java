package jpolanco.springbootapp.user.application.services.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.base.DeactivateUserUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeactivateUser implements DeactivateUserUC {
    private final UserQueryRepository queryRepository;
    private final UserCommandRepository commandRepository;

    @Override
    public Result<User> deactivate(User user, String reason) {
        user.deactivate(reason);
        var updatedUser = commandRepository.save(user);
        return Result.success(updatedUser);
    }

    @Override
    public Result<User> deactivateById(String userId, String reason) {
        var maybeUser = queryRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserAppError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        return deactivate(user, reason);
    }
}
