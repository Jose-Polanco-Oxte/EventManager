package jpolanco.applicationcore.user.infrastructure.components.providers;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.ServiceError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;
import jpolanco.applicationcore.user.application.errors.AccountError;
import jpolanco.applicationcore.user.application.ports.input.JwtProvider;
import jpolanco.applicationcore.user.application.ports.input.TokenGenerator;
import jpolanco.applicationcore.user.application.ports.output.TokenCommandRepository;
import jpolanco.applicationcore.user.domain.model.User;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtGenerator implements TokenGenerator {

    private final JwtProvider jwtProvider;
    private final TokenCommandRepository tokenCommandRepository;

    @Override
    public Result<UserTokenResponse, ServiceError> generateToken(User user) {
        String accessToken = jwtProvider.generateAccessToken(
                user.getUserId().getUUID(),
                user.getEmail().getValue(),
                user.getRoles().get());

        String refreshToken = jwtProvider.generateRefreshToken(
                user.getUserId().getUUID(),
                user.getEmail().getValue(),
                user.getRoles().get());
        tokenCommandRepository.save(refreshToken, user.getUserId().getUUID());
        return Result.success(new UserTokenResponse(accessToken, refreshToken));
    }

    @Override
    public Result<String, ServiceError> extractUsername(String token) {
        Optional<String> maybeUsername = jwtProvider.extractUsername(token);
        return maybeUsername.<Result<String, ServiceError>>map(Result::success)
                .orElseGet(() -> Result.failure(AccountError.invalidJwtToken()));
    }
}