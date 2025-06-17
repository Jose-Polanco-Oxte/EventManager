package jpolanco.springbootapp.user.application.uc;

import jpolanco.springbootapp.user.application.utils.UserUpdater;
import jpolanco.springbootapp.user.domain.model.User;

public interface UpdateProfileUC {
    /**
     * Updates a user
     *
     * @param user the user with changes to be updated
     * @return a Result containing the builder for update user and can be use like an api
     */
    UserUpdater setChanges(User user);
}
