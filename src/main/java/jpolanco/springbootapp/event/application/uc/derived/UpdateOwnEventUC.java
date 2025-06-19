package jpolanco.springbootapp.event.application.uc.derived;


import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.shared.domain.Result;

import java.io.InputStream;

public interface UpdateOwnEventUC {
    /**
     * Updates the event with the given ID, owned by the creator with the given ID.
     *
     * @param eventId    The ID of the event to update.
     * @param creatorId  The ID of the creator who owns the event.
     * @param request    The request containing the updated event details.
     * @param imageStream An InputStream for the new image associated with the event.
     * @return A Result containing the updated Event if successful, or an error if not.
     */
    Result<Event> setChanges(String eventId, String creatorId, UpdateEventRequest request, InputStream imageStream);
}
