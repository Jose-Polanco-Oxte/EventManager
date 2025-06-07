package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface GetAllEventsUC {
    /**
     * Retrieves all events for a specific user.
     *
     * @param userId the ID of the user whose events are to be retrieved
     * @return a Result containing a list of events or an error if the retrieval fails
     */
    Result<List<Event>> getAllEvents(String userId);
}
