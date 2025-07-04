package jpolanco.springbootapp.user.application.defaultservices.unique;

import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.application.ports.input.JwtProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.usecase.unique.GenerateToken;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.application.utils.TokenStatus;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class GenerateTokenDefault implements GenerateToken {
    private final JwtCommandRepository commandRepository;
    private final JwtProvider jwtProvider;

    @Override
    public Result<UserTokenResponse> create(User user) {
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        commandRepository.save(
                new TokenE(
                        refreshToken,
                        user,
                        TokenStatus.ACTIVE,
                        Instant.now()
                )
        );
        return Result.success(new UserTokenResponse(accessToken, refreshToken));
    }
}
