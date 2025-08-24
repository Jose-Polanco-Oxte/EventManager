package jpolanco.applicationcore.user.application.services.interfaces.commands;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.RegisterRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserTokenResponse;

public interface RegisterAccount {
    String OPERATION = "REGISTER_ACCOUNT";

    /**
     * Registers a new user account based on the provided registration request.
     *
     * @param request The RegisterRequest object containing user registration details.
     * @return A UserTokenResponse object containing the registered user's token information.
     */
    UserTokenResponse registerUser(RegisterRequest request);
}
