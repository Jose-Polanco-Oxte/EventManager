package jpolanco.springbootapp.user.infrastructure.services.implementations;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import jpolanco.springbootapp.user.application.ports.output.UserRepository;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import jpolanco.springbootapp.user.infrastructure.components.utils.JwtManager;
import jpolanco.springbootapp.user.infrastructure.errors.UserInfrastructureError;
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
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authentication;

    @Override
    public Result<UserTokenResponse> createTokens(User user) {
        var accessToken = jwtManager.generateAccessToken(user);
        var refreshToken = jwtManager.generateRefreshToken(user);
        jwtRepository.save(
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
        if (userEmail == null) {
            return Result.failure(UserInfrastructureError.INVALID_CLAIMS_REFRESH_TOKEN);
        }
        var maybeUser = userRepository.findByEmail(userEmail);
        if (maybeUser.isEmpty()) {
            return Result.failure(UserInfrastructureError.USER_NOT_FOUND);
        }
        var user = maybeUser.get();
        if (!jwtManager.isTokenValid(refreshToken, user.getEmail())) {
            return Result.failure(UserInfrastructureError.INVALID_REFRESH_TOKEN);
        }
        jwtRepository.revokeByToken(refreshToken);
        return createTokens(user);
    }
}
