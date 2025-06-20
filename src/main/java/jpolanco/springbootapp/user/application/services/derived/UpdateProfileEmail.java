package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfileEmailUC;
import jpolanco.springbootapp.user.application.utils.UserValidator;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileEmail implements UpdateProfileEmailUC {
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;
    private final UserValidator userValidator;

    @Override
    public Result<User> setEmail(String userId, UpdateEmailRequest request) {
        var maybeUser = userValidator.onUpdateEmailIsValid(userId, request.email());
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        if (user.isSuspended()) {
            return Result.failure(UserAppError.USER_SUSPENDED);
        }
        user.changeEmail(request.email());
        var updatedUser = userRepository.save(user);
        jwtRepository.revokeAllByUserId(userId);
        return Result.success(updatedUser);
    }
}
