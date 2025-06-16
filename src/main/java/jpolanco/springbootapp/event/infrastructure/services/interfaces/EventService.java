package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.SimpleResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.SlicePageResponseDto;

import java.io.InputStream;
import java.util.List;

public interface EventService {

    Result<EventResponseDto> createEvent(EventCreationDto eventDto, String creatorId, InputStream imageStream);

    Result<EventResponseDto> updateEvent(String eventId, UpdateEventDto eventDto, InputStream imageStream);

    Result<EventResponseDto> updateEventClearStaff(String eventId, UpdateEventDto eventDto, InputStream imageStream);

    Result<EventResponseDto> updateEventAddStaff(String eventId, UpdateEventDto eventDto, InputStream imageStream);

    Result<EventResponseDto> getEventById(String eventId);

    List<EventResponseDto> getAllEvents();

    // This method is used for pagination with a page-based approach
    SlicePageResponseDto<EventResponseDto> getEvents(int page, int size, String sortBy, String sortOrder);

    // This method is used for pagination with a cursor-based approach
    CursorPageResponseDto<EventResponseDto, String> getEvents(String cursor, int size, String sortBy, String sortOrder);

    // This method is used for pagination with a page-based approach for public events
    SlicePageResponseDto<EventResponseDto> getPublicEvents(int page, int size, String sortBy, String sortOrder);

    // This method is used for pagination with a cursor-based approach for public events
    CursorPageResponseDto<EventResponseDto, String> getPublicEvents(String cursor, int size, String sortBy, String sortOrder);

    Result<SimpleResponseDto> deleteEventById(String eventId);

    Result<SimpleResponseDto> eventCancelation(String eventId, String reason);

    Result<SimpleResponseDto> eventReactivation(String eventId);
}