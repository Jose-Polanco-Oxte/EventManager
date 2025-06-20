package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;

public interface UpdateProfileEmailUC {
    /**
     * Updates the email of a user.
     *
     * @param userId The ID of the user whose email is to be updated.
     * @param request The request containing the new email.
     * @return A Result containing the updated User or an error if the update fails.
     */
    Result<User> setEmail(String userId, UpdateEmailRequest request);
}
