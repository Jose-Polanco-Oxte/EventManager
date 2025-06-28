package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;

public interface SuspendUserUC {
    /**
     * Suspends a user with the given user and reason.
     *
     * @param user   the User to suspend
     * @param reason the reason for suspension
     * @return a Result containing a list of EventNotifications or an error if the suspension failed
     */
    Result<List<EventNotification>> suspend(User user, String reason);

    /**
     * Suspends a user by their ID with the given reason.
     *
     * @param userId the ID of the user to suspend
     * @param reason the reason for suspension
     * @return a Result containing a list of EventNotifications or an error if the suspension failed
     */
    Result<List<EventNotification>> suspendById(String userId, String reason);
}