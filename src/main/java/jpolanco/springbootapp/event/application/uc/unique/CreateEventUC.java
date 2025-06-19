package jpolanco.springbootapp.event.application.uc.unique;

import jpolanco.springbootapp.event.application.ports.input.request.StaffRequest;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationRequest;
import jpolanco.springbootapp.shared.domain.Result;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;

public interface CreateEventUC {
    /**
     * Creates a new event with the provided details.
     *
     * @param request The details of the event to be created.
     * @param creatorId The ID of the user creating the event.
     * @return A Result containing the created Event or an error if creation fails.
     */
    Result<Event> create(
            String creatorId,
            EventCreationRequest request,
            InputStream imageStream
    );
}