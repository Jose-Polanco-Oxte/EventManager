package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.user.domain.model.User;

import java.util.Optional;

public interface GetUserByEmailUC {
    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return a Result object containing the User if found, or an error message if not found
     */
    Optional<User> get(String email);
}
