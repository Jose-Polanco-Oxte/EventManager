package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request.ChangePasswordRequest;

import java.util.List;

public interface UpdateProfilePasswordUC {
    /**
     * Updates the user's password.
     *
     * @param userId  The ID of the user whose password is to be updated.
     * @param request The request containing the new password.
     * @return A Result containing a list of EventNotifications if successful, or an error if it fails.
     */
    Result<List<EventNotification>> setPassword(String userId, ChangePasswordRequest request);
}