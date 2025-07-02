package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.shared.application.AppError;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.JwtProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.uc.unique.GenerateJwtUC;
import jpolanco.springbootapp.user.application.uc.unique.RefreshTokenUC;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshToken implements RefreshTokenUC {
    private final UserQueryRepository userQueryRepository;
    private final JwtCommandRepository jwtCommandRepository;
    private final GenerateJwtUC generateJwtUC;
    private final JwtProvider jwtProvider;

    @Override
    public Result<UserTokenResponse> refresh(String refreshToken) {
        var userEmail = jwtProvider.extractUsername(refreshToken);
        if (userEmail == null) return Result.failure(AppError.UNPROCESSABLE_ENTITY
                .withField("RefreshToken")
                .withMessage("failed to extract userId email from refresh token"));

        var maybeUser = userQueryRepository.findByEmail(userEmail);
        if (maybeUser.isEmpty()) return Result.failure(AppError.RESOURCE_NOT_FOUND
                .withField("Email")
                .withMessage("failed to find userId with email " + userEmail));

        var user = maybeUser.get();
        if (!jwtProvider.isTokenValid(refreshToken, user.getEmail())) {
            return Result.failure(AppError.CONFLICT
                    .withField("RefreshToken")
                    .withMessage("refresh token is invalid or expired"));
        }

        jwtCommandRepository.revokeByToken(refreshToken);
        return generateJwtUC.create(user);
    }
}
