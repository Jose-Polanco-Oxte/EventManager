package jpolanco.springbootapp.event.application.services.base;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventCommandRepository;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.base.CancelEventUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.event.domain.model.domainevents.EventCanceled;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelEvent implements CancelEventUC {
    private final EventQueryRepository queryRepository;
    private final EventCommandRepository commandRepository;

    @Override
    public Result<Event> cancel(Event event, String reason) {
        if (event == null) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        if (event.isCancelled()) {
            return Result.failure(EventAppError.EVENT_ALREADY_CANCELLED);
        }
        event.cancel();
        event.recordEvent(new EventCanceled(event.getEventId(), reason));
        commandRepository.save(event);
        return Result.success(event);
    }

    @Override
    public Result<Event> cancelById(String eventId, String reason) {
        var maybeEvent = queryRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        var event = maybeEvent.get();
        if (event.isCancelled()) {
            return Result.failure(EventAppError.EVENT_ALREADY_CANCELLED);
        }
        event.cancel();
        event.recordEvent(new EventCanceled(event.getEventId(), reason));
        commandRepository.save(event);
        return Result.success(event);
    }
}
