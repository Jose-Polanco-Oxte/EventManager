package jpolanco.springbootapp.event.application.uc.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.utils.CursorPageResult;
import jpolanco.springbootapp.shared.utils.PageResult;

public interface GetOwnEventsUC {
    /**
     * Retrieves a paginated list of events created by the user.
     *
     * @param pagePaginationRequest the pagination request containing page number, size, and sorting option
     * @param creatorId the ID of the user who created the events
     * @return a PageResult containing a list of events created by the user
     */
    PageResult<Event> get(PagePaginationRequest pagePaginationRequest, String creatorId);

    /**
     * Retrieves a cursor-based paginated list of events created by the user.
     *
     * @param cursorPaginationRequest the cursor pagination request containing cursor, size, and sorting options
     * @param creatorId the ID of the user who created the events
     * @return a CursorPageResult containing a list of events created by the user
     */
    CursorPageResult<Event, String> get(CursorPaginationRequest<String> cursorPaginationRequest, String creatorId);
}
