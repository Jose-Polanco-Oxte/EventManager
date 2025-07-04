package jpolanco.springbootapp.user.application.use_case.unique;

import jpolanco.springbootapp.user.domain.model.value_objects.User;

import java.util.Optional;
import java.util.UUID;

public interface GetUserByIdUC {
    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId the unique identifier of the user
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> get(UUID userId);
}