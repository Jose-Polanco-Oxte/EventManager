package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface SuspendUserUC {
    /**
     * Suspends a user with the given User object and reason.
     *
     * @param user the User object to suspend
     * @param reason the reason for suspension
     * @return a Result containing the suspended User or an error if the suspension failed
     */
    Result<User> suspend(User user, String reason);

    /**
     * Suspends a user with the given userId and reason.
     *
     * @param userId the ID of the user to suspend
     * @param reason the reason for suspension
     * @return a Result containing the suspended User or an error if the suspension failed
     */
    Result<User> suspendById(String userId, String reason);
}