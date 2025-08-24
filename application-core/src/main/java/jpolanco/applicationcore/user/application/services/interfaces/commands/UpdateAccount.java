package jpolanco.applicationcore.user.application.services.interfaces.commands;

import jpolanco.applicationcore.user.domain.dto.UpdateAllUser;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.UUID;

public interface UpdateAccount {
    String OPERATION = "UPDATE_ACCOUNT";

    /**
     * Updates the user account identified by targetAccount under the authority of mainAccount with the provided details.
     *
     * @param mainAccount   The UUID of the main account performing the update.
     * @param targetAccount The UUID of the account to be updated.
     * @param request       The UpdateAllUser object containing the new details for the user account.
     * @return A UserResponse object containing details of the updated account.
     */
    UserResponse setChanges(UUID mainAccount, UUID targetAccount, UpdateAllUser request);
}