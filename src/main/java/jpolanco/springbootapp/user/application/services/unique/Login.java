package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtQueryRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.LoginUC;
import jpolanco.springbootapp.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Login implements LoginUC {
    private final UserQueryRepository queryRepository;
    private final JwtQueryRepository jwtQueryRepository;
    private final EncoderProvider passwordEncoder;

    @Override
    public Result<User> login(String email, String password) {
        var maybeUser = queryRepository.findByEmail(email);
        if (maybeUser.isEmpty()) {
            return Result.failure(AppError.RESOURCE_NOT_FOUND
                    .withField("User")
                    .concatMessage("with email " + email));
        }

        var user = maybeUser.get();
        int sessions = jwtQueryRepository.countSessionsByUserId(user.getId());
        if (sessions >= 5) return Result.failure(AppError.UNAUTHORIZED
                    .withMessage("User has too many active sessions."));

        if (user.isInactive()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("User has been inactive."));

        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("User has been suspended."));

        if (!passwordEncoder.matches(password, user.getEncodedPassword())) return Result.failure(AppError.UNAUTHORIZED
                .withMessage("Passwords do not match."));

        return Result.success(user);
    }
}
