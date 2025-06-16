package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.CursorPageResult;

public interface CGetMyEventsUC {
    /**
     * Retrieves all events created by the user, with cursor-based pagination and sorting options.
     *
     * @param cursor the cursor for pagination, used to fetch the next set of results
     * @param creatorId the ID of the user who created the events
     * @param size the number of events per page
     * @param sortBy the field to sort the events by
     * @param sortOrder the order of sorting (e.g., ascending or descending)
     * @return a CursorPageResult containing a list of Event objects
     */
    CursorPageResult<Event, String> getMyEvents(String cursor, String creatorId, int size, String sortBy, String sortOrder);
}
