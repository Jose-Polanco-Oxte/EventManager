package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface MyEventsService {

    Result<Void> deleteMyEvent(String userId, String eventId);

    Result<Dto> updateMyEvent(String userId, String eventId, String imageFileName, UpdateEventDto eventDto);

    Result<Dto> updateMyEventClearStaff(String userId, String eventId, String imageFileName, UpdateEventDto eventDto);

    Result<Dto> updateMyEventAddStaff(String userId, String eventId, String imageFileName, UpdateEventDto eventDto);

    Result<List<Dto>> getMyEvents(String userId, int page, int size);

    Result<List<Dto>> getMyEventsByCategory(String userId, String category, String modality, int page, int size);

    Result<List<Dto>> getMyEventsByTitle(String userId, String title, String modality, int page, int size);

    Result<List<Dto>> getMyEventsByLocation(String userId, String locationName, String modality, int page, int size);
}
