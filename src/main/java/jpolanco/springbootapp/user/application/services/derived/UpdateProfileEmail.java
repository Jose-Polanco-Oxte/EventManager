package jpolanco.springbootapp.user.application.services.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.errors.UserAppError;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserCommandRepository;
import jpolanco.springbootapp.user.application.uc.derived.UpdateProfileEmailUC;
import jpolanco.springbootapp.user.application.utils.UserValidation;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProfileEmail implements UpdateProfileEmailUC {
    private final UserCommandRepository commandRepository;
    private final JwtCommandRepository jwtCommandRepository;
    private final UserValidation userValidation;

    @Override
    public Result<User> setEmail(String userId, UpdateEmailRequest request) {
        var maybeUser = userValidation.onUpdateEmailIsValid(userId, request.email());
        if (maybeUser.isFailure()) {
            return Result.failure(maybeUser.getError());
        }
        var user = maybeUser.getValue();
        if (user.isSuspended()) {
            return Result.failure(UserAppError.USER_SUSPENDED);
        }
        user.changeEmail(request.email());
        var updatedUser = commandRepository.save(user);
        jwtCommandRepository.revokeAllByUserId(userId);
        return Result.success(updatedUser);
    }
}
