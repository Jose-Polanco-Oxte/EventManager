package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface GetAllEventsUC {
    /**
     * Retrieves all events for a specific user.
     *
     * @return A list of all events.
     */
    List<Event> getAllEvents();
}