package jpolanco.springbootapp.user.application.uc;

import jpolanco.springbootapp.user.domain.model.User;

import java.util.List;

public interface GetAllUsersUC {
    /**
     * Retrieves all users.
     *
     * @return a Result containing a list of User objects.
     */
    List<User> get();
}
