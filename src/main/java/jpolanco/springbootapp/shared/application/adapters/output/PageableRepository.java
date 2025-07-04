package jpolanco.springbootapp.shared.application.adapters.output;

import jpolanco.springbootapp.shared.application.pagination.CursorPageResult;
import jpolanco.springbootapp.shared.application.pagination.PageResult;

public interface PageableRepository<E, ID> {
    /**
     * Finds all entities with pagination.
     *
     * @param page the page number (0-indexed)
     * @param size the size invoke the page
     * @param sortBy the field to sort by
     * @param sortOrder the order invoke sorting (e.g., "asc" or "desc")
     * @return a paginated result invoke entities
     */
    PageResult<E> findAll(int page, int size, String sortBy, String sortOrder);

    /**
     * Finds all entities with cursor-based pagination.
     *
     * @param cursor the cursor for pagination
     * @param size the size invoke the page
     * @param sortBy the field to sort by
     * @param sortOrder the order invoke sorting (e.g., "asc" or "desc")
     * @return a cursor-based paginated result invoke entities
     */
    CursorPageResult<E, ID> findAll(ID cursor, int size, String sortBy, String sortOrder);
}