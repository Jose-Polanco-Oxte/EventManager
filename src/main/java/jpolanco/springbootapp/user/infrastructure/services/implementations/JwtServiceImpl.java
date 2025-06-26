package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.errors.InfrastructureError;
import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.ports.output.UserQueryRepository;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import jpolanco.springbootapp.user.infrastructure.components.utils.JwtManager;
import jpolanco.springbootapp.user.infrastructure.services.interfaces.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtManager jwtManager;
    private final JwtCommandRepository jwtCommandRepository;
    private final UserQueryRepository userQueryRepository;
    private final AuthenticationManager authentication;

    @Override
    public Result<UserTokenResponse> createTokens(User user) {
        var accessToken = jwtManager.generateAccessToken(user);
        var refreshToken = jwtManager.generateRefreshToken(user);
        jwtCommandRepository.save(
                new TokenE(
                        refreshToken,
                        user.getId(),
                        TokenStatus.ACTIVE,
                        Instant.now()
                )
        );
        return Result.success(new UserTokenResponse(accessToken, refreshToken));
    }

    @Override
    public Result<UserTokenResponse> authenticate(User user, String password) {
        var response = createTokens(user);
        if (response.isFailure()) {
            return Result.failure(response.getError());
        }
        authentication.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        password
                )
        );
        return Result.success(response.getValue());
    }

    @Override
    public Result<UserTokenResponse> refreshTokens(String refreshToken) {
        var userEmail = jwtManager.extractUsername(refreshToken);
        if (userEmail == null) return Result.failure(InfrastructureError.EXTERNAL_SERVICE_ERROR
                .withField("RefreshToken")
                .withMessage("failed to extract user email from refresh token"));

        var maybeUser = userQueryRepository.findByEmail(userEmail);
        if (maybeUser.isEmpty()) return Result.failure(InfrastructureError.EXTERNAL_SERVICE_ERROR
                .withField("Email")
                .withMessage("failed to find user with email " + userEmail));

        var user = maybeUser.get();
        if (!jwtManager.isTokenValid(refreshToken, user.getEmail())) {
            return Result.failure(InfrastructureError.EXTERNAL_SERVICE_ERROR
                    .withField("RefreshToken")
                    .withMessage("refresh token is invalid or expired"));
        }

        jwtCommandRepository.revokeByToken(refreshToken);
        return createTokens(user);
    }
}
