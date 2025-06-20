package jpolanco.springbootapp.event.application.services.derived;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.base.DeleteEventUC;
import jpolanco.springbootapp.event.application.uc.derived.DeleteOwnEventByIdUC;
import jpolanco.springbootapp.event.domain.model.valueobjects.EventStatus;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteOwnEvent implements DeleteOwnEventByIdUC {
    private final EventRepository eventRepository;
    private final DeleteEventUC deleteEventUC;

    @Override
    public Result<Void> delete(String creatorId, String eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        if (!event.get().getCreatorId().equals(creatorId)) {
            return Result.failure(EventAppError.EVENT_NOT_BELONG_TO_USER);
        }
        // Check if the event has already been cancelled or completed
        if (!(event.get().getStatus().equals(EventStatus.CANCELLED) || event.get().getStatus().equals(EventStatus.COMPLETED))) {
            return Result.failure(EventAppError.EVENT_NOT_CANCELLED_OR_COMPLETED);
        }
        var deleteResult = deleteEventUC.delete(event.get());
        if (deleteResult.isFailure()) {
            return Result.failure(deleteResult.getError());
        }
        return Result.success();
    }
}
