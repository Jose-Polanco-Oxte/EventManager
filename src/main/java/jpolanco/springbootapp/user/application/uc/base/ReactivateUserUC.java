package jpolanco.springbootapp.user.application.uc.base;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.user.domain.model.User;

public interface ReactivateUserUC {
    /**
     * Reactivates a user with the given reason.
     *
     * @param user the User to reactivate
     * @return a Result containing the reactivated User or an error if the reactivation failed
     */
    Report reactivate(User user);

    /**
     * Reactivates a user by their ID.
     *
     * @param userId the ID of the user to reactivate
     * @return a Report containing the reactivated User or an error if the reactivation failed
     */
    Report reactivateById(String userId);
}
