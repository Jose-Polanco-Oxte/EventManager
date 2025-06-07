package jpolanco.springbootapp.event.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.shared.application.Dto;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.List;

public interface EventService {
    Result<Dto> createEvent(EventCreationDto eventDto, String creatorId, String imageFileName);
    Result<Dto> updateEvent(String eventId, UpdateEventDto eventDto, String imageFileName);
    Result<Dto> updateEventClearStaff(String eventId, UpdateEventDto eventDto, String imageFileName);
    Result<Dto> updateEventAddStaff(String eventId, UpdateEventDto eventDto, String imageFileName);
    Result<Dto> getEventById(String eventId);
    Result<List<Dto>> getAllEvents(String userId);
    Result<Dto> deleteEventById(String eventId);
}
