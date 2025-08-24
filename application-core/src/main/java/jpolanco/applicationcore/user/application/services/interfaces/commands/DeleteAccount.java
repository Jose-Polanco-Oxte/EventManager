package jpolanco.applicationcore.user.application.services.interfaces.commands;

import java.util.UUID;

public interface DeleteAccount {
    String OPERATION = "DELETE_ACCOUNT";

    /**
     * Delete a user account by its UUID.
     *
     * @param mainAccount   the account performing the deletion
     * @param targetAccount the account to be deleted
     * @param reason        the reason for deletion
     */
    void deleteById(UUID mainAccount, UUID targetAccount, String reason);

    /**
     * Delete the main account by its UUID.
     *
     * @param mainAccount the account to be deleted
     * @param reason      the reason for deletion
     */
    void deleteMainById(UUID mainAccount, String reason);
}