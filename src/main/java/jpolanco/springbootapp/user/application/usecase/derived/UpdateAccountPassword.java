package jpolanco.springbootapp.user.application.usecase.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;

import java.util.List;
import java.util.UUID;

public interface UpdateAccountPassword {
    /**
     * Sets a new password for the user identified by the given UUID.
     *
     * @param userId  The UUID of the user whose password is to be changed.
     * @param request The request containing the old and new passwords.
     * @return A Result containing a list of EventNotifications if successful, or an error if not.
     */
    Result<List<EventNotification>> setPassword(UUID userId, ChangePasswordRequest request);
}