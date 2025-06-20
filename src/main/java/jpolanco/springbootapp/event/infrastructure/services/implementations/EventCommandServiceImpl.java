package jpolanco.springbootapp.event.infrastructure.services.implementations;

import jpolanco.springbootapp.event.application.uc.base.CancelEventUC;
import jpolanco.springbootapp.event.application.uc.base.DeleteEventUC;
import jpolanco.springbootapp.event.application.uc.base.UpdateEventUC;
import jpolanco.springbootapp.event.application.uc.unique.CreateEventUC;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.EventCreationRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.EventCommandService;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class EventCommandServiceImpl implements EventCommandService {
    private final CreateEventUC createEventUC;
    private final UpdateEventUC updateEventUC;
    private final DeleteEventUC deleteEventUC;
    private final CancelEventUC cancelEventUC;
    private final DomainEventsPublisher publisher;
    private final EventDtoCreator eventDtoCreator;

    @Override
    @Transactional
    public Result<EventResponse> createEvent(String creatorId, EventCreationRequest request, InputStream imageStream) {
        var createdEvent = createEventUC.create(creatorId, request, imageStream);
        if (createdEvent.isFailure()) {
            return Result.failure(createdEvent.getError());
        }
        var event = createdEvent.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(createdEvent.getValue()));
    }

    @Override
    @Transactional
    public Result<EventResponse> updateEvent(String eventId, UpdateEventRequest request, InputStream imageStream) {
        var eventUpdated = updateEventUC.setChangesById(eventId, request, imageStream);
        if (eventUpdated.isFailure()) {
            return Result.failure(eventUpdated.getError());
        }
        var event = eventUpdated.getValue();
        event.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(event));
    }


    @Override
    public Result<Void> deleteEventById(String eventId) {
        var result = deleteEventUC.deleteById(eventId);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success();
    }

    @Override
    public Result<Void> cancelEvent(String eventId, String reason) {
        var result = cancelEventUC.cancelById(eventId, reason);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success();
    }

    @Override
    public Result<Void> reactivateEvent(String eventId) {
        return null;
    }
}
