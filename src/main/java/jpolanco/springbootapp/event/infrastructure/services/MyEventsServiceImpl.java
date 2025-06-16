package jpolanco.springbootapp.event.infrastructure.services;

import jpolanco.springbootapp.event.application.uc.*;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.Modality;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventDto;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.MyEventsService;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.SimpleResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.CursorPageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.dto.SlicePageResponseDto;
import jpolanco.springbootapp.shared.infrastructure.mappers.CursorPageCreator;
import jpolanco.springbootapp.shared.infrastructure.mappers.SlicePageCreator;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyEventsServiceImpl implements MyEventsService {

    private final DeleteMyEventByIdUC deleteMyEventByIdUC;
    private final PGetMyEventsUC PGetMyEventsUC;
    private final CGetMyEventsUC CGetMyEventsUC;
    private final UpdateMyEventUC updateMyEventUC;
    private final EventDtoCreator eventDtoCreator;
    private final CancelMyEventUC cancelMyEventUC;
    private final DomainEventsPublisher publisher;
    private final SlicePageCreator<Event, EventResponseDto> slicePageCreator;
    private final CursorPageCreator<Event, EventResponseDto, String> cursorPageCreator;

    @Override
    @Transactional
    public Result<SimpleResponseDto> deleteMyEvent(String userId, String eventId) {
        var result = deleteMyEventByIdUC.delete(userId, eventId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(new SimpleResponseDto("Event deleted successfully"));
    }

    @Override
    @Transactional
    public Result<EventResponseDto> updateMyEvent(String userId, String eventId, InputStream imageStream, UpdateEventDto eventDto) {
        var eventUpdated = updateMyEventUC.setChanges(eventId, userId)
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
    public Result<EventResponseDto> updateMyEventClearStaff(String userId, String eventId, InputStream imageStream, UpdateEventDto eventDto) {
        var eventUpdated = updateMyEventUC.setChanges(eventId, userId)
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
    public Result<EventResponseDto> updateMyEventAddStaff(String userId, String eventId, InputStream imageStream, UpdateEventDto eventDto) {
        var eventUpdated = updateMyEventUC.setChanges(eventId, userId)
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
    public SlicePageResponseDto<EventResponseDto> getMyEvents(String userId, int page, int size, String sortBy, String sortOrder) {
        return slicePageCreator.create(
                PGetMyEventsUC.getMyEvents(userId, page, size, sortBy, sortOrder),
                eventDtoCreator
        );
    }

    @Override
    public CursorPageResponseDto<EventResponseDto, String> getMyEvents(String cursor, String userId, int size, String sortBy, String sortOrder) {
        return cursorPageCreator.create(
                CGetMyEventsUC.getMyEvents(cursor, userId, size, sortBy, sortOrder),
                eventDtoCreator
        );
    }

    @Override
    public List<EventResponseDto> getMyEventsByCategory(String userId, String category, String modality, int page, int size) {
        return null;
    }

    @Override
    public List<EventResponseDto> getMyEventsByTitle(String userId, String title, String modality, int page, int size) {
        return null;
    }

    @Override
    public List<EventResponseDto> getMyEventsByLocation(String userId, String locationName, String modality, int page, int size) {
        return null;
    }

    @Override
    @Transactional
    public Result<SimpleResponseDto> myEventCancelation(String userId, String eventId, String reason) {
        var result = cancelMyEventUC.cancelEvent(eventId, userId, reason);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        var event = result.getValue();
        event.pullEvents().forEach(publisher::publish);
        event.clearEvents();
        return Result.success(new SimpleResponseDto("Event canceled successfully"));
    }
}
