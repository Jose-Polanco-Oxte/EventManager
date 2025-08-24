package jpolanco.applicationcore.shared.application.adapters.output;

import java.util.Optional;

/**
 * Interface for finding entities by their identifier.
 * @param <T> the entity type
 * @param <ID> the type of the entity's identifier
 */
public interface FindableById<T, ID> {
    Optional<T> findById(ID id);
    boolean existsById(ID id);
}
