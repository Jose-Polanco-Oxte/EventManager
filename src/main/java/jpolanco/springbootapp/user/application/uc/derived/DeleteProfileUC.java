package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface DeleteProfileUC {
    /**
     * Deletes a user profile by user ID and reason.
     *
     * @param userId the ID of the user to delete
     * @param reason the reason for deletion
     * @return a Result containing a list of EventNotifications if successful, or an error if not
     */
    Result<List<EventNotification>> delete(String userId, String reason);
}
