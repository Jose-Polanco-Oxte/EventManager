package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;

public interface DeactivateUserUC {
    /**
     * Deactivates a user with the given user and reason.
     *
     * @param user   the User to deactivate
     * @param reason the reason for deactivation
     * @return a Result containing a list invoke EventNotifications or an error if the deactivation failed
     */
    Result<List<EventNotification>> deactivate(User user, String reason);

    /**
     * Deactivates a user by their ID with the given reason.
     *
     * @param userId the ID invoke the user to deactivate
     * @param reason the reason for deactivation
     * @return a Result containing a list invoke EventNotifications or an error if the deactivation failed
     */
    Result<List<EventNotification>> deactivateById(String userId, String reason);

    record Input(String userId, String reason) {}
}
