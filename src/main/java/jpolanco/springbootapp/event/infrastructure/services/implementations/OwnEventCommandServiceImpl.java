package jpolanco.springbootapp.event.infrastructure.services.implementations;

import jpolanco.springbootapp.event.application.uc.derived.CancelOwnEventUC;
import jpolanco.springbootapp.event.application.uc.derived.DeleteOwnEventByIdUC;
import jpolanco.springbootapp.event.application.uc.derived.UpdateOwnEventUC;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.OwnEventCommandService;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.infrastructure.publisher.DomainEventsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnEventCommandServiceImpl implements OwnEventCommandService {

    private final UpdateOwnEventUC updateOwnEventUC;
    private final DeleteOwnEventByIdUC deleteOwnEventByIdUC;
    private final EventDtoCreator eventDtoCreator;
    private final CancelOwnEventUC cancelOwnEventUC;
    private final DomainEventsPublisher publisher;

    @Transactional
    @Override
    public Result<EventResponse> updateEvent(String creatorId, String eventId, UpdateEventRequest request, InputStream imageStream) {
        var updatedEvent = updateOwnEventUC.setChanges(creatorId, eventId, request, imageStream);
        if (updatedEvent.isFailure()) {
            return Result.failure(updatedEvent.getError());
        }
        var event = updatedEvent.getValue();
        publisher.publishAll(event.pullEvents());
        return Result.success(eventDtoCreator.create(event));
    }

    @Transactional
    @Override
    public Result<Void> deleteEvent(String creatorId, String eventId, String reason) {
        var result = deleteOwnEventByIdUC.delete(creatorId, eventId, reason);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        List<EventNotification> appEvents = result.getValue();
        publisher.publishAll(appEvents);
        return Result.success();
    }

    @Transactional
    @Override
    public Result<Void> cancelEvent(String creatorId, String eventId, String reason) {
        var cancelledEvent = cancelOwnEventUC.cancel(creatorId, eventId, reason);
        if (cancelledEvent.isFailure()) {
            return Result.failure(cancelledEvent.getError());
        }
        var event = cancelledEvent.getValue();
        publisher.publishAll(event.pullEvents());
        return Result.success();
    }

    @Override
    public Result<Void> reactivateEvent(String creatorId, String eventId) {
        return null;
    }
}
