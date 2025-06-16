package jpolanco.springbootapp.event.infrastructure.services;

import jpolanco.springbootapp.event.application.uc.*;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.Modality;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventService;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.SimpleResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.SlicePageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.mappers.CursorPageCreator;
import jpolanco.springbootapp.shared.infrastructure.mappers.SlicePageCreator;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.dto.SimpleResponseCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final CreateEventUC createEventUC;
    private final UpdateEventUC updateEventUC;
    private final DeleteEventByIdUC deleteEventByIdUC;
    private final GetEventByIdUC getEventByIdUC;
    private final GetAllEventsUC getAllEventsUC;
    private final EventDtoCreator eventDtoCreator;
    private final PGetEventsUC PGetEventsUC;
    private final CGetEventsUC CGetEventsUC;
    private final PGetPublicEventsUC PGetPublicEventsUC;
    private final CGetPublicEventsUC CGetPublicEventsUC;
    private final CancelEventUC cancelEventUC;
    private final SimpleResponseCreator simpleResponseCreator;
    private final SlicePageCreator<Event, EventResponseDto> slicePageCreator;
    private final CursorPageCreator<Event, EventResponseDto, String> cursorPageCreator;
    private final DomainEventsPublisher publisher;


    @Override
    @Transactional
    public Result<EventResponseDto> createEvent(EventCreationDto eventDto, String creatorId, InputStream imageStream) {
        var createdEvent = createEventUC.create(eventDto, creatorId, imageStream);
        if (createdEvent.isFailure()) {
            return Result.failure(createdEvent.getError());
        }
        var event = createdEvent.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(createdEvent.getValue()));
    }

    @Override
    @Transactional
    public Result<EventResponseDto> updateEvent(String eventId, UpdateEventDto eventDto, InputStream imageStream) {
        var eventUpdated = updateEventUC.setChanges(eventId)
                .title(eventDto.title())
                .description(eventDto.description())
                .schedule(eventDto.schedule())
                .duration(eventDto.durationInSeconds())
                .location(
                        eventDto.locationName(),
                        eventDto.locationCity(),
                        eventDto.locationCountry(),
                        eventDto.latitude(),
                        eventDto.longitude()
                )
                .categories(eventDto.categories())
                .isPublic(eventDto.isPublic())
                .enableComments(eventDto.enableComments())
                .modality(Modality.create(eventDto.modality()))
                .staff(eventDto.staffs())
                .changePicture(imageStream)
                .setMaxAttendees(eventDto.maxAttendees())
                .update();
        if (eventUpdated.isFailure()) {
            return Result.failure(eventUpdated.getError());
        }
        var event = eventUpdated.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(event));
    }

    @Override
    @Transactional
    public Result<EventResponseDto> updateEventClearStaff(String eventId, UpdateEventDto eventDto, InputStream imageStream) {
        var eventUpdated = updateEventUC.setChanges(eventId)
                .title(eventDto.title())
                .description(eventDto.description())
                .schedule(eventDto.schedule())
                .duration(eventDto.durationInSeconds())
                .location(
                        eventDto.locationName(),
                        eventDto.locationCity(),
                        eventDto.locationCountry(),
                        eventDto.latitude(),
                        eventDto.longitude()
                )
                .categories(eventDto.categories())
                .isPublic(eventDto.isPublic())
                .enableComments(eventDto.enableComments())
                .modality(Modality.create(eventDto.modality()))
                .clearStaff()
                .changePicture(imageStream)
                .setMaxAttendees(eventDto.maxAttendees())
                .update();
        if (eventUpdated.isFailure()) {
            return Result.failure(eventUpdated.getError());
        }
        var event = eventUpdated.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(event));
    }

    @Override
    @Transactional
    public Result<EventResponseDto> updateEventAddStaff(String eventId, UpdateEventDto eventDto, InputStream imageStream) {
        var eventUpdated = updateEventUC.setChanges(eventId)
                .title(eventDto.title())
                .description(eventDto.description())
                .schedule(eventDto.schedule())
                .duration(eventDto.durationInSeconds())
                .location(
                        eventDto.locationName(),
                        eventDto.locationCity(),
                        eventDto.locationCountry(),
                        eventDto.latitude(),
                        eventDto.longitude()
                )
                .categories(eventDto.categories())
                .isPublic(eventDto.isPublic())
                .enableComments(eventDto.enableComments())
                .modality(Modality.create(eventDto.modality()))
                .addStaff(eventDto.staffs())
                .changePicture(imageStream)
                .setMaxAttendees(eventDto.maxAttendees())
                .update();
        if (eventUpdated.isFailure()) {
            return Result.failure(eventUpdated.getError());
        }
        var event = eventUpdated.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(event));
    }

    @Override
    public Result<EventResponseDto> getEventById(String eventId) {
        var maybeEvent = getEventByIdUC.getById(eventId);
        if (maybeEvent.isFailure()) {
            return Result.failure(maybeEvent.getError());
        }
        var event = maybeEvent.getValue();
        return Result.success(eventDtoCreator.create(event));
    }

    @Override
    public List<EventResponseDto> getAllEvents() {
        var maybeEvents = getAllEventsUC.getAllEvents();
        if (maybeEvents.isEmpty()) {
            return List.of();
        }
        return maybeEvents.stream()
                .map(eventDtoCreator::create)
                .toList();
    }

    @Override
    public SlicePageResponseDto<EventResponseDto> getEvents(int page, int size, String sortBy, String sortOrder) {
        return slicePageCreator.create(
                PGetEventsUC.getEvents(page, size, sortBy, sortOrder),
                eventDtoCreator
        );
    }

    @Override
    public CursorPageResponseDto<EventResponseDto, String> getEvents(String cursor, int size, String sortBy, String sortOrder) {
        return cursorPageCreator.create(CGetEventsUC.getEvents(cursor, size, sortBy, sortOrder), eventDtoCreator);
    }

    @Override
    public SlicePageResponseDto<EventResponseDto> getPublicEvents(int page, int size, String sortBy, String sortOrder) {
        return slicePageCreator.create(
                PGetPublicEventsUC.getPublicEvents(page, size, sortBy, sortOrder),
                eventDtoCreator
        );
    }

    @Override
    public CursorPageResponseDto<EventResponseDto, String> getPublicEvents(String cursor, int size, String sortBy, String sortOrder) {
        return cursorPageCreator.create(
                CGetPublicEventsUC.getPublicEvents(cursor, size, sortBy, sortOrder),
                eventDtoCreator
        );
    }

    @Override
    @Transactional
    public Result<SimpleResponseDto> deleteEventById(String eventId) {
        var result = deleteEventByIdUC.deleteById(eventId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(simpleResponseCreator.create("Event deleted successfully"));
    }

    @Override
    @Transactional
    public Result<SimpleResponseDto> eventCancelation(String eventId, String reason) {
        var result = cancelEventUC.cancelEvent(eventId, reason);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var event = result.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(simpleResponseCreator.create("Event cancelled successfully"));
    }

    //Pending implementation for event reactivation
    @Override
    public Result<SimpleResponseDto> eventReactivation(String eventId) {
        return null;
    }
}
