package jpolanco.springbootapp.user.application.defaultservices.unique;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.input.AuthenticatorProvider;
import jpolanco.springbootapp.user.application.ports.input.EncoderProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtQueryRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.usecase.unique.GenerateToken;
import jpolanco.springbootapp.user.application.usecase.unique.Login;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginDefault implements Login {
    private final UserQueryRepository queryRepository;
    private final JwtQueryRepository jwtQueryRepository;
    private final EncoderProvider passwordEncoder;
    private final AuthenticatorProvider authentication;
    private final GenerateToken generateToken;

    @Override
    public Result<UserTokenResponse> login(LoginRequest request) {
        var maybeUser = queryRepository.findByEmail(request.email());
        if (maybeUser.isEmpty()) {
            return Result.failure(AppError.RESOURCE_NOT_FOUND
                    .withMessage("User with email " + request.email() + " does not exist"));
        }

        var user = maybeUser.get();
        int sessions = jwtQueryRepository.countSessionsByUserId(user.getId());
        if (sessions >= 5) return Result.failure(AppError.UNAUTHORIZED
                    .withMessage("User has too many active sessions."));

        if (user.isInactive()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("User has been inactive."));

        if (user.isSuspended()) return Result.failure(AppError.INVALID_OPERATION
                .withMessage("User has been suspended."));

        if (!passwordEncoder.matches(request.password(), user.getEncodedPassword())) return Result.failure(AppError.UNAUTHORIZED
                .withMessage("Passwords do not match."));

        authentication.authenticate(user.getEmail(), request.password());
        return generateToken.create(user);
    }
}
