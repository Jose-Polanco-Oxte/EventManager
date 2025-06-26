package jpolanco.springbootapp.user.application.uc.derived;

import jpolanco.springbootapp.shared.domain.Report;

public interface DeactivateProfileUC {
    /**
     * Deactivates a user profile by user ID with a reason.
     *
     * @param userId the ID of the user to deactivate
     * @param reason the reason for deactivation
     * @return a report containing the deactivated user or an error
     */
    Report deactivate(String userId, String reason);
}
