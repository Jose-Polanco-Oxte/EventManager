package jpolanco.springbootapp.event.application.services.derived;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.base.DeleteEventUC;
import jpolanco.springbootapp.event.application.uc.derived.DeleteOwnEventByIdUC;
import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteOwnEvent implements DeleteOwnEventByIdUC {
    private final EventQueryRepository queryRepository;
    private final DeleteEventUC deleteEventUC;

    @Override
    public Result<List<EventNotification>> delete(String creatorId, String eventId, String reason) {
        var maybeEvent = queryRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        var event = maybeEvent.get();
        if (!event.getCreatorId().equals(creatorId)) {
            return Result.failure(EventAppError.EVENT_NOT_BELONG_TO_USER);
        }
        // Check if the event has already been cancelled or completed
        if (!(event.isCancelled() & event.isCompleted())) {
            return Result.failure(EventAppError.EVENT_NOT_CANCELLED_OR_COMPLETED);
        }
        return deleteEventUC.delete(maybeEvent.get(), reason);
    }
}
