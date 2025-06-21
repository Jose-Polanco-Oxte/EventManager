package jpolanco.springbootapp.event.application.services.derived;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.base.UpdateEventUC;
import jpolanco.springbootapp.event.application.uc.derived.UpdateOwnEventUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.request.UpdateEventRequest;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class UpdateOwnEvent implements UpdateOwnEventUC {
    private final EventQueryRepository queryRepository;
    private final UpdateEventUC updateEventUC;

    @Override
    public Result<Event> setChanges(String eventId, String creatorId, UpdateEventRequest request, InputStream imageStream) {
        var maybeEvent = queryRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        if (!maybeEvent.get().getCreatorId().equals(creatorId)) {
            return Result.failure(EventAppError.EVENT_NOT_BELONG_TO_USER);
        }
        var event = maybeEvent.get();
        return updateEventUC.setChanges(event, request, imageStream);
    }
}
