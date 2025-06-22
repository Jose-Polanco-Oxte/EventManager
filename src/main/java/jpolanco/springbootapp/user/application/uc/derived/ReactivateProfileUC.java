package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface ReactivateProfileUC {
    /**
     *
     * Reactivates a user with the given userId and reason.
     * @param userId the ID of the user to reactivate
     * @return a Result containing the reactivated User or an error if the reactivation failed
     */
    Result<User> reactivate(String userId);
}
