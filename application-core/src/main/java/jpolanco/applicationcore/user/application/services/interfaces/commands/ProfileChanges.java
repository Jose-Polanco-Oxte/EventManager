package jpolanco.applicationcore.user.application.services.interfaces.commands;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangeNameRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;
import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.UUID;

public interface ProfileChanges {
    /**
     * Change the email of a user identified by userId.
     *
     * @param userId  the UUID of the user whose email is to be changed
     * @param request the request containing the new email information
     * @return the updated user response
     */
    UserResponse changeEmail(UUID userId, ChangeEmailRequest request);

    /**
     * Change the name of a user identified by userId.
     *
     * @param userId  the UUID of the user whose name is to be changed
     * @param request the request containing the new name information
     * @return the updated user response
     */
    UserResponse changeName(UUID userId, ChangeNameRequest request);

    /**
     * Change the password of a user identified by userId.
     *
     * @param userId  the UUID of the user whose password is to be changed
     * @param request the request containing the new password information
     * @return the updated user response
     */
    UserResponse changePassword(UUID userId, ChangePasswordRequest request);
}
