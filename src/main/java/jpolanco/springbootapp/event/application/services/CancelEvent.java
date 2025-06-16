package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.CancelEventUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.EventStatus;
import jpolanco.springbootapp.event.domain.model.domainevents.EventCanceled;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelEvent implements CancelEventUC {
    private final EventRepository eventRepository;
    @Override
    public Result<Event> cancelEvent(String eventId, String reason) {
        var maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        var event = maybeEvent.get();
        event.changeStatus(EventStatus.CANCELLED);
        event.recordEvent(new EventCanceled(event.getEventId(), reason));
        eventRepository.update(event);
        return Result.success(event);
    }
}
