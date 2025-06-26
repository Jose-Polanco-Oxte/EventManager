package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Report;

public interface ReactivateProfileUC {
    /**
     * Reactivates a user profile by user ID.
     *
     * @param userId the ID of the user to reactivate
     * @return a Report containing the reactivated User
     */
    Report reactivate(String userId);
}
