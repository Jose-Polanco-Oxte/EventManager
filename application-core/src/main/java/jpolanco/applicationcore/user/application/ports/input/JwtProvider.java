package jpolanco.applicationcore.user.application.ports.input;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for generating and validating JWT tokens.
 * Implementations should provide methods to create access and refresh tokens,
 * extract information from tokens, and validate token authenticity and expiration.
 */
public interface JwtProvider {
    String generateAccessToken(UUID id, String subject, List<String> roles);

    String generateRefreshToken(UUID id, String subject, List<String> roles);

    Optional<String> extractUsername(String token);

    Optional<UUID> extractId(String token);

    Optional<List<String>> extractRoles(String token);

    boolean isTokenValid(String token, String email);

    boolean isTokenExpired(String token);
}
