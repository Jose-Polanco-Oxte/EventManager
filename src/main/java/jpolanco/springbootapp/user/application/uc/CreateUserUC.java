package jpolanco.springbootapp.user.application.uc;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.model.User;

public interface CreateUserUC {
    /**
     * Creates a new user with the given details.
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param password the password of the user
     * @return a Result object containing the created User if successful, or an error message if not
     */
    Result<User> create(String firstName, String lastName, String email, String password);
}