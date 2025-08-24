package jpolanco.applicationcore.user.application.ports.output;

import jpolanco.applicationcore.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port interface for user query operations.
 * This interface defines methods for retrieving user entities based on various criteria.
 */
public interface UserQueryRepository {
    Optional<User> findById(Long id);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    Optional<User> findByIdUserFilter(Long id);

    Optional<User> findByUuidUserFilter(UUID uuid);

    Optional<User> findByEmailUserFilter(String email);

    Optional<User> findByIdAndNotDeleted(Long userId);

    Optional<User> findByUuidAndNotDeleted(UUID uuid);

    Optional<User> findByEmailAndNotDeleted(String email);

    List<User> searchByEmailUserFilter(String email, int Size);

    List<User> searchByNameUserFilter(String name, int Size);

    List<User> searchByEmail(String email, int Size);

    List<User> searchByName(String name, int Size);
}