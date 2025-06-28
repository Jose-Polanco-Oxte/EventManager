package jpolanco.springbootapp.event.application.uc.base;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.shared.domain.Result;

import java.io.InputStream;

public interface UpdateEventUC {
    /**
     * Updates an event with the given changes.
     * @param event the event with changes to apply
     * @return a Result containing the builder for update event and can be used like an API
     */
    Result<Event> setChanges(Event event, UpdateEventRequest request, InputStream inputStream);

    /**
     * Updates an event with the given ID and changes.
     * @param eventId the ID invoke the event to update
     * @param request the request containing the changes to apply
     * @return a Result containing the builder for update event and can be used like an API
     */
    Result<Event> setChangesById(String eventId, UpdateEventRequest request, InputStream inputStream);
}
