package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangeEmailRequest;

import java.util.List;
import java.util.UUID;

public interface UpdateProfileEmailUC {
    /**
     * Sets a new email for the user identified by the given UUID.
     *
     * @param userId  The UUID of the user whose email is to be changed.
     * @param request The request containing the new email.
     * @return A Result containing a list of EventNotifications if successful, or an error if not.
     */
    Result<List<EventNotification>> setEmail(UUID userId, ChangeEmailRequest request);
}
