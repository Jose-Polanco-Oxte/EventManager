package jpolanco.springbootapp.shared.application.adapters.output;

import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;

public interface PageableRepository<E, ID> {
    /**
     * Finds all entities with pagination.
     *
     * @param page the page number (0-indexed)
     * @param size the size of the page
     * @param sortBy the field to sort by
     * @param sortOrder the order of sorting (e.g., "asc" or "desc")
     * @return a paginated result of entities
     */
    PageResult<E> findAll(int page, int size, String sortBy, String sortOrder);

    /**
     * Finds all entities with cursor-based pagination.
     *
     * @param cursor the cursor for pagination
     * @param size the size of the page
     * @param sortBy the field to sort by
     * @param sortOrder the order of sorting (e.g., "asc" or "desc")
     * @return a cursor-based paginated result of entities
     */
    CursorPageResult<E, ID> findAll(String cursor, int size, String sortBy, String sortOrder);
}