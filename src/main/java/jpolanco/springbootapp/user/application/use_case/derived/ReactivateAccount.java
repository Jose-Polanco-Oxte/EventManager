package jpolanco.springbootapp.user.application.use_case.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;
import java.util.UUID;

public interface ReactivateProfileUC {
    /**
     * Reactivates a user profile with the given user UUID.
     *
     * @param userId the UUID of the user profile to reactivate
     * @return a Result containing a list of EventNotifications if the reactivation is successful
     */
    Result<List<EventNotification>> reactivate(UUID userId);
}
