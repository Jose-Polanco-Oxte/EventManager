package jpolanco.springbootapp.user.application.uc;

import jpolanco.springbootapp.user.application.utils.UserUpdater;

public interface UpdateProfileUC {
    /**
     * Updates a user with the given ID and changes.
     *
     * @param userId the ID of the user to update
     * @return a Result containing the builder for update user and can be use like an api
     */
    UserUpdater setChanges(String userId);
}
