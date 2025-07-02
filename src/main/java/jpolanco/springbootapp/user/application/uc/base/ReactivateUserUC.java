package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.value_objects.User;

import java.util.List;
import java.util.UUID;

public interface ReactivateUserUC {
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
