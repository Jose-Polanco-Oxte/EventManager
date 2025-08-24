package jpolanco.applicationcore.shared.application.adapters.output;

/**
 * Base repository interface for CRUD operations.
 *
 * @param <T>  the entity type
 * @param <ID> the type of the entity's identifier
 */
public interface BaseRepository<T, ID> extends FindableById<T, ID> {
    T save(T entity);

    void deleteById(ID id);
}
