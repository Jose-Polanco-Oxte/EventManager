package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.shared.domain.Result;

import java.io.InputStream;

public interface OwnEventCommandService {
    Result<EventResponse> updateEvent(String creatorId, String eventId, UpdateEventRequest request, InputStream imageStream);
    Result<Void> deleteEvent(String creatorId, String eventId, String reason);
    Result<Void> cancelEvent(String creatorId, String eventId, String reason);
    Result<Void> reactivateEvent(String creatorId, String eventId);
}
