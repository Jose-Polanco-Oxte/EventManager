package jpolanco.applicationcore.shared.infrastructure.utils.types;

/**
 * Interface representing a sortable value.
 * Provides methods to retrieve the sort value as a string
 * and to check if the value is unsorted.
 */
public interface SortValue {
    String getSortValue();

    boolean unsorted();
}
