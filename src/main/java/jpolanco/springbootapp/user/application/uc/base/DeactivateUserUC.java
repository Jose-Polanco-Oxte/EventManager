package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.domain.model.User;

public interface DeactivateUserUC {
    /**
     * Deactivates a user with the given user and reason.
     *
     * @param user   the User to deactivate
     * @param reason the reason for deactivation
     * @return a Report containing the deactivated User or an error if the deactivation failed
     */
    Report deactivate(User user, String reason);

    /**
     * Deactivates a user with the given userId and reason.
     *
     * @param userId the ID of the user to deactivate
     * @param reason the reason for deactivation
     * @return a Result containing the deactivated User or an error if the deactivation failed
     */
    Report deactivateById(String userId, String reason);
}
