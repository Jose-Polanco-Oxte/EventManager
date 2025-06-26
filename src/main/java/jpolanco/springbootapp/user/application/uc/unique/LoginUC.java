package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface LoginUC {
    /**
     * Logs in a user using their email and password.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return a Result containing the User if login is successful, or an error if login fails
     */
    Result<User> login(String email, String password);
}
