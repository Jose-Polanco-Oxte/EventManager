package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface DeactivateUserUC {
    /**
     * Deactivates a user with the given User object and reason.
     *
     * @param user the User object to deactivate
     * @param reason the reason for deactivation
     * @return a Result containing the deactivated User or an error if the deactivation failed
     */
    Result<User> deactivate(User user, String reason);

    /**
     * Deactivates a user with the given userId and reason.
     *
     * @param userId the ID of the user to deactivate
     * @param reason the reason for deactivation
     * @return a Result containing the deactivated User or an error if the deactivation failed
     */
    Result<User> deactivateById(String userId, String reason);
}
