package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface ReactivateProfileUC {
    /**
     * Reactivates a user's profile.
     *
     * @param userId The ID of the user whose profile is to be reactivated.
     * @return A Result containing a list of EventNotifications if successful, or an error if it fails.
     */
    Result<List<EventNotification>> reactivate(String userId);
}
