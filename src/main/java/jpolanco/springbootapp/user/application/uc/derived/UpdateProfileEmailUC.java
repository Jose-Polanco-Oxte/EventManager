package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;

import java.util.List;

public interface UpdateProfileEmailUC {
    /**
     * Updates the email of a user profile.
     *
     * @param userId  the ID of the user whose email is to be updated
     * @param request the request containing the new email
     * @return a Result containing a list of EventNotifications if successful, or an error if not
     */
    Result<List<EventNotification>> setEmail(String userId, ChangeEmailRequest request);
}
