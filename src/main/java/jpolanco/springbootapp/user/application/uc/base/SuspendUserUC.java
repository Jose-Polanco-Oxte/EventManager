package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.domain.model.User;

public interface SuspendUserUC {
    /**
     * Suspends a user with the given reason.
     *
     * @param user   the user to suspend
     * @param reason the reason for suspension
     * @return a Report containing the suspended User or an error if the suspension failed
     */
    Report suspend(User user, String reason);

    /**
     * Suspends a user by their ID with the given reason.
     *
     * @param userId the ID of the user to suspend
     * @param reason the reason for suspension
     * @return a Report containing the suspended User or an error if the suspension failed
     */
    Report suspendById(String userId, String reason);
}