package jpolanco.springbootapp.event.application.uc.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;

public interface GetEventsUC {
    /**
     * Retrieves a paginated list of events.
     *
     * @param pagePaginationRequest the pagination request containing page number and size
     * @return a PageResult containing a list of events
     */
    PageResult<Event> get(PagePaginationRequest pagePaginationRequest);

    /**
     * Retrieves a cursor-based paginated list of events.
     *
     * @param cursorPaginationRequest the pagination request containing cursor, size, sortBy, and sortOrder
     * @return a CursorPageResult containing a list of events with a cursor for pagination
     */
    CursorPageResult<Event, String> get(CursorPaginationRequest<String> cursorPaginationRequest);
}