package jpolanco.applicationcore.user.application.ports.output;

import java.util.UUID;

/**
 * Interface for managing user tokens in the data store.
 * Implementations should provide methods to save, delete, revoke, and expire tokens.
 */
public interface TokenCommandRepository {
    void save(String token, UUID userId);

    void deleteByToken(String token);

    void revokeByToken(String token);

    void deleteAllByUserId(UUID userId);

    void deleteAllByStatus(String status);

    void expireToken(String token);
}
