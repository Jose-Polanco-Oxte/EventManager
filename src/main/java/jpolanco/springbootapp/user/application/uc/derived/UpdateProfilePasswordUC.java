package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdatePasswordRequest;

public interface UpdateProfilePasswordUC {
    /**
     * Updates the password of a user.
     *
     * @param userId The ID of the user whose password is to be updated.
     * @param request The request containing the new password.
     * @return A Result containing the updated User or an error if the update fails.
     */
    Result<User> setPassword(String userId, UpdatePasswordRequest request);
}