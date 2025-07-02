package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface LoginUC {
    /**
     * Authenticates a user based on the provided login request.
     *
     * @param request the login request containing user credentials
     * @return a Result containing UserTokenResponse if successful, or an error if authentication fails
     */
    Result<UserTokenResponse> login(LoginRequest request);
}
