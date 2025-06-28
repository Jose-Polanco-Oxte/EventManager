package jpolanco.springbootapp.event.application.uc.unique;

import jpolanco.springbootapp.event.domain.model.Event;

import java.util.List;

public interface GetAllEventsUC {
    /**
     * Retrieves all events for a specific user.
     *
     * @return A list invoke all events.
     */
    List<Event> get();
}