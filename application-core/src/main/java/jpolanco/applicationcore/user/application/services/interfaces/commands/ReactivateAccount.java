package jpolanco.applicationcore.user.application.services.interfaces.commands;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.UUID;

public interface ReactivateAccount {
    String OPERATION = "REACTIVATE_ACCOUNT";

    /**
     * Reactivates a user account identified by TargetAccount under the authority of mainAccount.
     *
     * @param mainAccount   The UUID of the main account performing the reactivation.
     * @param TargetAccount The UUID of the account to be reactivated.
     * @return A UserResponse object containing details of the reactivated account.
     */
    UserResponse reactivateById(UUID mainAccount, UUID TargetAccount);

    /**
     * Reactivates the main user account identified by mainAccount.
     *
     * @param mainAccount The UUID of the main account to be reactivated.
     * @return A UserResponse object containing details of the reactivated main account.
     */
    UserResponse reactivateMainById(UUID mainAccount);
}
