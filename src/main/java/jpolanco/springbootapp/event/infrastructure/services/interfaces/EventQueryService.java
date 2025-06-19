package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.SlicePageResponseDto;

import java.util.List;
import java.util.Optional;

public interface EventQueryService {
    Optional<EventResponse> getEventById(String eventId);
    List<EventResponse> getAllEvents();
    SlicePageResponseDto<EventResponse> getEventsByPages(int page, int size, String sortBy, String sortOrder);
    CursorPageResponseDto<EventResponse, String> getEventsByCursorBased(String cursor, int size, String sortBy, String sortOrder);
    SlicePageResponseDto<EventResponse> getPublicEventsByPages(int page, int size, String sortBy, String sortOrder);
    CursorPageResponseDto<EventResponse, String> getPublicEventsByCursorBased(String cursor, int size, String sortBy, String sortOrder);
}