package jpolanco.applicationcore.user.application.ports.output;

import jpolanco.applicationcore.user.domain.model.User;

/**
 * Port interface for user command operations.
 * This interface defines methods for saving and deleting user entities.
 */
public interface UserCommandRepository {
    User save(User entity);

    void deleteById(Long id);
}
