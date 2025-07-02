package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;
import java.util.UUID;

public interface DeactivateProfileUC {
    /**
     * Deactivates a user profile with the given user UUID and reason.
     *
     * @param userId the UUID of the user profile to deactivate
     * @param reason the reason for deactivation
     * @return a Result containing a list of EventNotifications if the deactivation is successful
     */
    Result<List<EventNotification>> deactivate(UUID userId, String reason);
}
