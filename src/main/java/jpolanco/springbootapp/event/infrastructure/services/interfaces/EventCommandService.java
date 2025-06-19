package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.shared.domain.Result;

import java.io.InputStream;

public interface EventCommandService {
    Result<EventResponse> createEvent(String creatorId, EventCreationRequest request, InputStream imageStream);
    Result<EventResponse> updateEvent(String eventId, UpdateEventRequest request, InputStream imageStream);
    Result<Void> deleteEventById(String eventId);
    Result<Void> cancelEvent(String eventId, String reason);
    Result<Void> reactivateEvent(String eventId);
}
