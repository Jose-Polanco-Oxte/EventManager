package jpolanco.applicationcore.user.application.services.interfaces.commands;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface Refresh {
    String OPERATION = "REFRESH_TOKEN";

    /**
     * Refreshes the user token using the provided authorization header.
     *
     * @param authHeader The authorization header containing the current token.
     * @return A UserTokenResponse object containing the new token information.
     */
    UserTokenResponse refresh(String authHeader);
}
