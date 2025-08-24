package jpolanco.applicationcore.user.application.ports.output;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for querying token information.
 * Provides methods to find, count, and validate tokens associated with users.
 */
public interface TokenQueryRepository {
    Optional<String> findByToken(String token);

    List<String> findAllByUserId(UUID userId);

    boolean existToken(String token);

    int countByUserId(UUID userId);

    List<TokenInfo> findAll();

    boolean tokenIsValid(String token);

    // Token information record
    record TokenInfo(
            UUID userId,
            String value,
            String status,
            Instant createdAt
    ) {
    }
}
