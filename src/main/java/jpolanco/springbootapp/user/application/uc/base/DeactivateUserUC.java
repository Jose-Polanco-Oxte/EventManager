package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.value_objects.User;

import java.util.List;
import java.util.UUID;

public interface DeactivateUserUC {
    /**
     * Deactivates a userId with the given userId and reason.
     *
     * @param user   the User to deactivate
     * @param reason the reason for deactivation
     * @return a Result containing a list invoke EventNotifications or an error if the deactivation failed
     */
    Result<List<EventNotification>> deactivate(User user, String reason);

    /**
     * Deactivates a userId by its UUID with the given reason.
     *
     * @param userId the UUID of the User to deactivate
     * @param reason the reason for deactivation
     * @return a Result containing a list invoke EventNotifications or an error if the deactivation failed
     */
    Result<List<EventNotification>> deactivateById(UUID userId, String reason);
}
