package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.utils.PageResult;

public interface PGetMyEventsUC {
    /**
     * Retrieves all events created by the user with the specified creator, paginated and sorted.
     *
     * @param creatorId the ID of the user whose events are to be retrieved
     * @param page the page number to retrieve
     * @param size the number of events per page
     * @param sortBy the field to sort the events by
     * @param sortOrder the order of sorting (e.g., ascending or descending)
     * @return a PageResult containing a list of Event objects created by the user
     */
    PageResult<Event> getMyEvents(String creatorId, int page, int size, String sortBy, String sortOrder);
}
