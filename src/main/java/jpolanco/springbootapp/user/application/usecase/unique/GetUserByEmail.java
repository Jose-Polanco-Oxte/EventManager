package jpolanco.springbootapp.user.application.usecase.unique;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;

import java.util.Optional;

public interface GetUserByEmail {
    /**
     * Retrieves a userId by their email address.
     *
     * @param email the email address invoke the userId to retrieve
     * @return a Result object containing the User if found, or an error message if not found
     */
    Optional<User> get(String email);
}
