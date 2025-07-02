package jpolanco.springbootapp.user.application.services.unique;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.application.ports.input.JwtProvider;
import jpolanco.springbootapp.user.application.ports.output.JwtCommandRepository;
import jpolanco.springbootapp.user.application.uc.unique.GenerateJwtUC;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.application.utils.TokenStatus;
import jpolanco.springbootapp.user.domain.model.value_objects.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class GenerateJwt implements GenerateJwtUC {
    private final JwtCommandRepository commandRepository;
    private final JwtProvider jwtProvider;

    @Override
    public Result<UserTokenResponse> create(User user) {
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        commandRepository.save(
                new TokenE(
                        refreshToken,
                        user.getId(),
                        TokenStatus.ACTIVE,
                        Instant.now()
                )
        );
        return Result.success(new UserTokenResponse(accessToken, refreshToken));
    }
}
