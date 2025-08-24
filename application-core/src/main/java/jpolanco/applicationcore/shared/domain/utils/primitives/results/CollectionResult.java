package jpolanco.applicationcore.shared.domain.utils.primitives.results;

import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;

import java.util.List;


/**
 * A generic class to represent the result of operations that involve collections.
 *
 * @param <V>  The type of items in the collection (e.g., entities or DTOs).
 * @param <VO> The type of the updated object after the operation.
 *             <p> {@code updated} - The updated object after the operation.
 *             <p> {@code removed} - A list of items that were removed during the operation.
 *             <p> {@code added} - A list of items that were added during the operation.
 *             <p> {@code notFound} - A list of identifiers for items that were not found during the operation.
 *             <p> {@code errors} - A list of domain errors that occurred during the operation.
 */
public record CollectionResult<V, VO>(
        VO updated,
        List<V> removed,
        List<V> added,
        List<String> notFound,
        List<DomainError> errors
) {
    public static <V, VO> CollectionResult<V, VO> success(VO updated, List<V> removed, List<V> added, List<String> notFound) {
        return new CollectionResult<>(updated, removed, added, notFound, null); // No errors on success
    }

    public static <V, VO> CollectionResult<V, VO> failure(List<DomainError> errors) {
        return new CollectionResult<>(null, null, null, null, errors);
    }

    public static <V, VO> CollectionResult<V, VO> failure(DomainError errors) {
        return new CollectionResult<>(null, null, null, null, List.of(errors));
    }

    public boolean isFailure() {
        return !(errors == null || errors.isEmpty()); // True if there are errors
    }
}
