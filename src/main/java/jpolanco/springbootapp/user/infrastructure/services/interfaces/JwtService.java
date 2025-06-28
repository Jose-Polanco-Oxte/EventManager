package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

import java.util.Map;

public interface JwtService {
    Result<Map<String, String>> createTokens(User user);

    Result<Map<String, String>> authenticate(User user, String password);

    Result<Map<String, String>> refreshTokens(String refreshToken);
}
