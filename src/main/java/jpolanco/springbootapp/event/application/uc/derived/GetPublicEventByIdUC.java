package jpolanco.springbootapp.event.application.uc.derived;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface GetPublicEventByIdUC {
    /**
     * Retrieves a public event by its ID.
     *
     * @param eventId the ID of the event to be retrieved
     * @return a Result containing the Event if found, or an error if not found
     */
    Result<Event> get(String eventId);
}
