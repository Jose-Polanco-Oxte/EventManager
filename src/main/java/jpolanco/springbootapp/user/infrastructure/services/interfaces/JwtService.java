package jpolanco.springbootapp.user.infrastructure.services.interfaces;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface JwtService {
    Result<UserTokenResponse> createTokens(User user);

    Result<UserTokenResponse> authenticate(User user, String password);

    Result<UserTokenResponse> refreshTokens(String refreshToken);
}
