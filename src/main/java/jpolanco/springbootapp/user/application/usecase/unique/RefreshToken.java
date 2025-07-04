package jpolanco.springbootapp.user.application.usecase.unique;

import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface RefreshToken {
    /**
     * Refreshes the user token using the provided refresh token.
     *
     * @param refreshToken the refresh token to be used for generating a new user token
     * @return a Result containing UserTokenResponse if successful, or an error if the refresh fails
     */
    Result<UserTokenResponse> refresh(String refreshToken);
}
