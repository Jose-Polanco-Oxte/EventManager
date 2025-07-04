package jpolanco.springbootapp.user.application.usecase.unique;

import jpolanco.springbootapp.user.domain.model.valueobjects.User;

import java.util.Optional;
import java.util.UUID;

public interface GetUserByUUID {
    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId the unique identifier of the user
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> get(UUID userId);
}