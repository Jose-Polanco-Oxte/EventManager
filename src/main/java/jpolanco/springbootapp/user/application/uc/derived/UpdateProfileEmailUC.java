package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.UpdateEmailRequest;

public interface UpdateProfileEmailUC {
    /**
     * Updates the email of a user identified by userId.
     *
     * @param userId  the ID of the user whose email is to be updated
     * @param request the request containing the new email
     * @return a Report containing the updated User or an error if the operation fails
     */
    Report setEmail(String userId, UpdateEmailRequest request);
}
