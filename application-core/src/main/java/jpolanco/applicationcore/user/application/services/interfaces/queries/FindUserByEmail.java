package jpolanco.applicationcore.user.application.services.interfaces.queries;

import jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.Optional;

/**
 * Service interface for finding a user by their email address.
 * Provides a method to retrieve user information based on the provided email.
 * <p> {@link #find(String email)} - Finds a user by email and returns an Optional containing UserResponse if found.
 */
public interface FindUserByEmail {
    Optional<UserResponse> find(String email);
}
