package jpolanco.springbootapp.user.application.usecase.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;

import java.util.List;
import java.util.UUID;

public interface DeleteAccount {
    /**
     * Deletes a user profile with the given user UUID and reason.
     *
     * @param userId the UUID of the user profile to delete
     * @param reason the reason for deletion
     * @return a Result containing a list of EventNotifications if the deletion is successful
     */
    Result<List<EventNotification>> delete(UUID userId, String reason);
}
