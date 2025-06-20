package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfilePasswordUC;
import jpolanco.springbootapp.user.application.utils.UserValidator;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfilePassword implements UpdateProfilePasswordUC {
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;
    private final EncoderProvider encoderProvider;
    private final UserValidator userValidator;

    @Override
    public Result<User> setPassword(String userId, UpdatePasswordRequest request) {
        var maybeUser = userValidator.onUpdatePasswordIsValid(userId, request.newPassword(), request.oldPassword());
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        if (user.isSuspended()) {
            return Result.failure(UserAppError.USER_SUSPENDED);
        }
        var encodedPassword = encoderProvider.encode(request.newPassword());
        user.changePassword(encodedPassword);
        var updatedUser = userRepository.save(user);
        jwtRepository.revokeAllByUserId(userId);
        return Result.success(updatedUser);
    }
}
