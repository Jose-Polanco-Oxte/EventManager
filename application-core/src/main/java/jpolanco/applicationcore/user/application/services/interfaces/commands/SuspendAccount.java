package jpolanco.applicationcore.user.application.services.interfaces.commands;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.UUID;

public interface SuspendAccount {
    String OPERATION = "SUSPEND_ACCOUNT";

    /**
     * Suspends a user account identified by targetAccount under the authority of mainAccount.
     *
     * @param mainAccount   The UUID of the main account performing the suspension.
     * @param targetAccount The UUID of the account to be suspended.
     * @param reason        The reason for suspending the account.
     * @return A UserResponse object containing details of the suspended account.
     */
    UserResponse suspendById(UUID mainAccount, UUID targetAccount, String reason);
}