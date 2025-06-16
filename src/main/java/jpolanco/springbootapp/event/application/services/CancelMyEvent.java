package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.CancelMyEventUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.EventStatus;
import jpolanco.springbootapp.event.domain.model.domainevents.EventCanceled;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelMyEvent implements CancelMyEventUC {
    private final EventRepository eventRepository;
    @Override
    public Result<Event> cancelEvent(String eventId, String userId, String reason) {
        var maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        if (!maybeEvent.get().getCreatorId().equals(userId)) {
            return Result.failure(EventAppError.EVENT_NOT_BELONG_TO_USER);
        }
        var event = maybeEvent.get();
        event.changeStatus(EventStatus.CANCELLED);
        event.recordEvent(new EventCanceled(event.getEventId(), reason));
        eventRepository.update(event);
        return Result.success(event);
    }
}
