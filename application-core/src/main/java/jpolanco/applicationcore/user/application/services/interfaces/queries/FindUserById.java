package jpolanco.applicationcore.user.application.services.interfaces.queries;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for finding a user by their unique identifier (UUID).
 * Provides a method to retrieve user information based on the provided user ID.
 * <p> {@link #find(UUID userId)} - Finds a user by their UUID and returns an Optional containing UserResponse if found.
 */
public interface FindUserById {
    Optional<UserResponse> find(UUID userId);
}
