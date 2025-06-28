package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface DeactivateProfileUC {
    /**
     * Deactivates a user profile.
     *
     * @param userId The ID of the user whose profile is to be deactivated.
     * @param reason The reason for deactivation.
     * @return A Result containing a list of EventNotifications if successful, or an error if it fails.
     */
    Result<List<EventNotification>> deactivate(String userId, String reason);
}
