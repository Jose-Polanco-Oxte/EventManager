package jpolanco.springbootapp.event.infrastructure.services.implementations;

import jpolanco.springbootapp.event.application.uc.derived.CancelOwnEventUC;
import jpolanco.springbootapp.event.application.uc.derived.DeleteOwnEventByIdUC;
import jpolanco.springbootapp.event.application.uc.derived.UpdateOwnEventUC;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.OwnEventCommandService;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.dto.SimpleResponseDto;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class OwnEventCommandServiceImpl implements OwnEventCommandService {

    private final UpdateOwnEventUC updateOwnEventUC;
    private final DeleteOwnEventByIdUC deleteOwnEventByIdUC;
    private final EventDtoCreator eventDtoCreator;
    private final CancelOwnEventUC cancelOwnEventUC;
    private final DomainEventsPublisher publisher;

    @Override
    public Result<EventResponse> updateEvent(String creatorId, String eventId, UpdateEventRequest request, InputStream imageStream) {
        var event = updateOwnEventUC.setChanges(creatorId, eventId, request, imageStream);
        if (event.isFailure()) {
            return Result.failure(event.getError());
        }
        var updatedEvent = event.getValue();
        updatedEvent.pullEvents().forEach(publisher::publish);
        return Result.success(eventDtoCreator.create(updatedEvent));
    }

    @Override
    public Result<Void> deleteEvent(String creatorId, String eventId) {
        var event = deleteOwnEventByIdUC.delete(creatorId, eventId);
        if (event.isFailure()) {
            return Result.failure(event.getError());
        }
        return Result.success();
    }

    @Override
    public Result<Void> cancelEvent(String creatorId, String eventId, String reason) {
        var event = cancelOwnEventUC.cancel(creatorId, eventId, reason);
        if (event.isFailure()) {
            return Result.failure(event.getError());
        }
        var cancelledEvent = event.getValue();
        cancelledEvent.pullEvents().forEach(publisher::publish);
        return Result.success();
    }

    @Override
    public Result<Void> reactivateEvent(String creatorId, String eventId) {
        return null;
    }
}
