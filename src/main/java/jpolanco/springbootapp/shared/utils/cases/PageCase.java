package jpolanco.springbootapp.shared.utils.cases;

import jpolanco.springbootapp.shared.infrastructure.dto.request.CursorPaginationRequest;
import jpolanco.springbootapp.shared.infrastructure.dto.request.PagePaginationRequest;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;

public interface PageCase<E, C> {
    /**
     * Executes the use case to retrieve paginated results.
     *
     * @param request the pagination request containing parameters for pagination
     * @return a PageResult containing the paginated entities
     */
    PageResult<E> byPages(PagePaginationRequest request);

    /**
     * Executes the use case to retrieve paginated results using cursor-based pagination.
     *
     * @param request the cursor pagination request containing parameters for pagination
     * @return a CursorPageResult containing the paginated entities
     */
    CursorPageResult<E, C> byCursor(CursorPaginationRequest<C> request);
}
