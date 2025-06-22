package jpolanco.springbootapp.event.application.uc.base;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

public interface GetEventByIdUC {
    /**
     * Retrieves an event by its ID.
     *
     * @param eventId the ID of the event to be retrieved
     * @return the Event object if found, or null if not found
     */
    Result<Event> get(String eventId);
}
