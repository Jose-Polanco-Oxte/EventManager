package jpolanco.springbootapp.event.application.uc;

import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.EventCreationDto;
import jpolanco.springbootapp.shared.domain.Result;

public interface CreateEventUC {
    /**
     * Creates a new event with the given details.
     *
     * @param event the details of the event to be created
     * @return a Result object containing the created Event if successful, or an error message if not
     */
    Result<Event> create(EventCreationDto event, String creatorId, String pictureFileName);
}