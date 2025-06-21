package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface DeleteProfileUC {
    /**
     * Deletes the profile of the user with the specified ID.
     *
     * @param userId the ID of the user whose profile is to be deleted
     * @return a Result indicating success or failure of the deletion operation
     */
    Result<List<EventNotification>> delete(String userId, String reason);
}
