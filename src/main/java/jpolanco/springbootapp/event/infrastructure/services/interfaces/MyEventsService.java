package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.SimpleResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.SlicePageResponseDto;

import java.io.InputStream;
import java.util.List;

public interface MyEventsService {

    Result<SimpleResponseDto> deleteMyEvent(String userId, String eventId);

    Result<EventResponseDto> updateMyEvent(String userId, String eventId, InputStream imageStream, UpdateEventDto eventDto);

    Result<EventResponseDto> updateMyEventClearStaff(String userId, String eventId, InputStream imageStream, UpdateEventDto eventDto);

    Result<EventResponseDto> updateMyEventAddStaff(String userId, String eventId, InputStream imageStream, UpdateEventDto eventDto);

    SlicePageResponseDto<EventResponseDto> getMyEvents(String userId, int page, int size, String sortBy, String sortOrder);

    CursorPageResponseDto<EventResponseDto, String> getMyEvents(String userId, String cursor, int size, String sortBy, String sortOrder);

    List<EventResponseDto> getMyEventsByCategory(String userId, String category, String modality, int page, int size);

    List<EventResponseDto> getMyEventsByTitle(String userId, String title, String modality, int page, int size);

    List<EventResponseDto> getMyEventsByLocation(String userId, String locationName, String modality, int page, int size);

    Result<SimpleResponseDto> myEventCancelation(String userId, String eventId, String reason);
}
