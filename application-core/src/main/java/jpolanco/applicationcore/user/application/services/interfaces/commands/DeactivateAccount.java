package jpolanco.applicationcore.user.application.services.interfaces.commands;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.UUID;

public interface DeactivateAccount {
    String OPERATION = "DEACTIVATE_ACCOUNT";

    /**
     * Deactivate a user account by its UUID.
     *
     * @param mainAccount   the account performing the deactivation
     * @param targetAccount the account to be deactivated
     * @param reason        the reason for deactivation
     * @return the deactivated user response
     */
    UserResponse deactivateById(UUID mainAccount, UUID targetAccount, String reason);

    /**
     * Deactivate the main account by its UUID.
     *
     * @param mainAccount the account to be deactivated
     * @param reason      the reason for deactivation
     * @return the deactivated user response
     */
    UserResponse deactivateMainById(UUID mainAccount, String reason);
}