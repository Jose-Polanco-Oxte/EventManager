package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;

public interface UpdateProfilePasswordUC {
    /**
     * Updates the password of a user identified by userId.
     *
     * @param userId  the ID of the user whose password is to be updated
     * @param request the request containing the new password and old password
     * @return a Report containing the changes or an error if the operation fails
     */
    Report setPassword(String userId, UpdatePasswordRequest request);
}