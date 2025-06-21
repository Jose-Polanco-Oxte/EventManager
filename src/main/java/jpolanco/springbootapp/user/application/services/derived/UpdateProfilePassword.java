package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfilePasswordUC;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.domain.domain_events.UserPasswordChanged;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfilePassword implements UpdateProfilePasswordUC {
    private final UserCommandRepository commandRepository;
    private final JwtCommandRepository jwtCommandRepository;
    private final EncoderProvider encoderProvider;
    private final UserValidation userValidation;

    @Override
    public Result<User> setPassword(String userId, UpdatePasswordRequest request) {
        var maybeUser = userValidation.onUpdatePasswordIsValid(userId, request.newPassword(), request.oldPassword());
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        if (user.isSuspended()) {
            return Result.failure(UserAppError.USER_SUSPENDED);
        }
        var encodedPassword = encoderProvider.encode(request.newPassword());
        user.changeEncodedPassword(encodedPassword);
        var updatedUser = commandRepository.save(user);
        jwtCommandRepository.revokeAllByUserId(userId);
        return Result.success(updatedUser);
    }
}
