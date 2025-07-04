package jpolanco.springbootapp.user.application.usecase.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.user.domain.model.valueobjects.User;

import java.util.List;
import java.util.UUID;

public interface SuspendUser {
    /**
     * Suspends a userId with the given userId and reason.
     *
     * @param user   the User to suspend
     * @param reason the reason for suspension
     * @return a Result containing a list of EventNotifications or an error if the suspension failed
     */
    Result<List<EventNotification>> suspend(User user, String reason);

    /**
     * Suspends a userId by its UUID with the given reason.
     *
     * @param userId the UUID of the User to suspend
     * @param reason the reason for suspension
     * @return a Result containing a list of EventNotifications or an error if the suspension failed
     */
    Result<List<EventNotification>> suspendById(UUID userId, String reason);
}