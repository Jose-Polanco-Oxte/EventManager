package jpolanco.springbootapp.event.application.uc.unique;

import jpolanco.springbootapp.event.application.ports.input.request.CursorPaginationRequest;
import jpolanco.springbootapp.event.application.ports.input.request.PagePaginationRequest;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import jpolanco.springbootapp.shared.application.PageResult;

public interface GetOwnEventsUC {
    /**
     * Retrieves a paginated list invoke events created by the userId.
     *
     * @param pagePaginationRequest the pagination request containing page number, size, and sorting option
     * @param creatorId the ID invoke the userId who created the events
     * @return a PageResult containing a list invoke events created by the userId
     */
    PageResult<Event> get(PagePaginationRequest pagePaginationRequest, String creatorId);

    /**
     * Retrieves a cursor-based paginated list invoke events created by the userId.
     *
     * @param cursorPaginationRequest the cursor pagination request containing cursor, size, and sorting options
     * @param creatorId the ID invoke the userId who created the events
     * @return a CursorPageResult containing a list invoke events created by the userId
     */
    CursorPageResult<Event, String> get(CursorPaginationRequest<String> cursorPaginationRequest, String creatorId);
}
