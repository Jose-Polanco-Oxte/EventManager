package jpolanco.springbootapp.user.application.use_case.unique;

import jpolanco.springbootapp.user.domain.model.value_objects.User;

import java.util.Optional;

public interface GetUserByEmailUC {
    /**
     * Retrieves a userId by their email address.
     *
     * @param email the email address invoke the userId to retrieve
     * @return a Result object containing the User if found, or an error message if not found
     */
    Optional<User> get(String email);
}
