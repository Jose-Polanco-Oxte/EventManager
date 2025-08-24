package jpolanco.applicationcore.shared.infrastructure.utils;

import jpolanco.applicationcore.shared.infrastructure.utils.types.Direction;
import jpolanco.applicationcore.shared.infrastructure.utils.types.SortValue;

/**
 * Interface representing a pageable request with pagination and sorting information.
 * <p>{@link #getPage()} - Returns the current page number (0-based index).
 * <p>{@link #getSize()} - Returns the number of items per page.
 * <p>{@link #getSortBy()} - Returns the field by which to sort the results.
 * <p>{@link #getDirection()} - Returns the direction of sorting (ascending or descending).
 * <p>{@link SortValue} - Enum representing sortable fields.
 * <p>{@link Direction} - Enum representing sorting directions (ASC, DESC).
 */
public interface PageableRequest {
    int getPage();

    int getSize();

    SortValue getSortBy();

    Direction getDirection();
}