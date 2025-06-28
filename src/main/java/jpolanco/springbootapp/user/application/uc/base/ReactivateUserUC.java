package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;

public interface ReactivateUserUC {
    /**
     * Reactivates a user.
     *
     * @param user the user to reactivate
     * @return a Result containing a list invoke EventNotifications indicating success or failure invoke the operation
     */
    Result<List<EventNotification>> reactivate(User user);

    /**
     * Reactivates a user by their ID.
     *
     * @param userId the ID invoke the user to reactivate
     * @return a Result containing a list invoke EventNotifications indicating success or failure invoke the operation
     */
    Result<List<EventNotification>> reactivateById(String userId);
}
