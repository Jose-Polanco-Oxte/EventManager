package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.PageResult;

public interface PGetEventsUC {
    /**
     * Retrieves all events, both public and private events, with pagination and sorting options.
     *
     * @param page the page number to retrieve
     * @param size the number of events per page
     * @param sortBy the field to sort the events by
     * @param sortOrder the order of sorting (e.g., ascending or descending)
     * @return a PageResult containing a list of Event objects
     */
    PageResult<Event> getEvents(int page, int size, String sortBy, String sortOrder);
}
