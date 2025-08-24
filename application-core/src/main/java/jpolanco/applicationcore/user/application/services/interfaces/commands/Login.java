package jpolanco.applicationcore.user.application.services.interfaces.commands;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.LoginRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface Login {
    String OPERATION = "LOGIN";

    /**
     * Login a user with the provided login request.
     *
     * @param request the login request containing user credentials
     * @return the user token response containing authentication tokens
     */
    UserTokenResponse loginUser(LoginRequest request);
}
