package jpolanco.applicationcore.user.application.components;

import jpolanco.applicationcore.shared.application.adapters.output.FindableById;
import jpolanco.applicationcore.shared.application.errors.NotFound;
import jpolanco.applicationcore.shared.application.exceptions.ApplicationExceptionHandler;
import jpolanco.applicationcore.shared.domain.utils.primitives.utils.Pair;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Utility class to find and return a pair of entities by their IDs.
 * Throws an ApplicationExceptionHandler if either entity is not found.
 *
 * @param <T>  the type of the entities to be found
 * @param <ID> the type of the entity IDs
 */
@RequiredArgsConstructor
public class FindPairs<T, ID> {

    private final FindableById<T, ID> repository;


    /**
     * Finds two entities by their IDs and returns them as a Pair.
     * If either entity is not found, throws an ApplicationExceptionHandler with a NotFound error.
     *
     * @param firstEntityId  the ID of the first entity
     * @param secondEntityId the ID of the second entity
     * @param entityType     the class type of the entities
     * @return a Pair containing the two found entities
     * @throws ApplicationExceptionHandler if either entity is not found
     */
    public Pair<T, T> find(ID firstEntityId, ID secondEntityId, Class<T> entityType) throws ApplicationExceptionHandler {
        Optional<T> firstEntity = repository.findById(firstEntityId);
        Optional<T> secondEntity = repository.findById(secondEntityId);

        String type = entityType.getSimpleName(); // Get the simple name of the class for error messages

        if (firstEntity.isEmpty()) {
            throw new ApplicationExceptionHandler(NotFound.entityNotFound(type, firstEntityId.toString()));
        }

        if (secondEntity.isEmpty()) {
            throw new ApplicationExceptionHandler(NotFound.entityNotFound(type, secondEntityId.toString()));
        }

        T first = firstEntity.get();
        T second = secondEntity.get();

        return Pair.of(first, second);
    }
}