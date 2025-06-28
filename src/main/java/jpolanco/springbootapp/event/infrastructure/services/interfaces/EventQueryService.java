package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.CursorPageResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.response.SlicePageResponse;

import java.util.Optional;

public interface EventQueryService {
    Optional<EventResponse> getEventById(String eventId);
    Optional<EventResponse> getPublicEventById(String eventId);
    SlicePageResponse<EventResponse> getEventsByPages(int page, int size, String sortBy, String sortOrder);
    CursorPageResponse<EventResponse, String> getEventsByCursorBased(String cursor, int size, String sortBy, String sortOrder);
    SlicePageResponse<EventResponse> getPublicEventsByPages(int page, int size, String sortBy, String sortOrder);
    CursorPageResponse<EventResponse, String> getPublicEventsByCursorBased(String cursor, int size, String sortBy, String sortOrder);
}