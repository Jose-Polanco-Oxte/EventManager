package jpolanco.springbootapp.user.application.usecase.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;

import java.util.List;
import java.util.UUID;

public interface ReactivateUser {
    /**
     * Reactivates a userId.
     *
     * @param user the userId to reactivate
     * @return a Result containing a list invoke EventNotifications indicating success or failure invoke the operation
     */
    Result<List<EventNotification>> reactivate(User user);

    /**
     * Reactivates a userId by its UUID.
     *
     * @param userId the UUID of the userId to reactivate
     * @return a Result containing a list invoke EventNotifications indicating success or failure invoke the operation
     */
    Result<List<EventNotification>> reactivateById(UUID userId);
}
