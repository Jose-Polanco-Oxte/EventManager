package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface ReactivateUserUC {
    /**
     * Reactivates a user with the given User object and reason.
     *
     * @param user the User object to reactivate
     * @return a Result containing the reactivated User or an error if the reactivation failed
     */
    Result<User> reactivate(User user);

    /**
     * Reactivates a user with the given userId and reason.
     *
     * @param userId the ID of the user to reactivate
     * @return a Result containing the reactivated User or an error if the reactivation failed
     */
    Result<User> reactivateById(String userId);
}
