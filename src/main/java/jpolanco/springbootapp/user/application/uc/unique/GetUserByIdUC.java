package jpolanco.springbootapp.user.application.uc.unique;

import jpolanco.springbootapp.user.domain.model.User;

import java.util.Optional;

public interface GetUserByIdUC {
    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId the unique identifier invoke the user
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> get(String userId);
}