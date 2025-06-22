package jpolanco.springbootapp.event.application.services.base;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventCommandRepository;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.base.RestoreEventUC;
import jpolanco.springbootapp.event.application.utils.EventUpdater;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestoreEvent implements RestoreEventUC {
    private final EventQueryRepository queryRepository;
    private final EventCommandRepository commandRepository;

    @Override
    public Result<Event> restore(Event event, String messageToAttendees) {
        if (event == null) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        if (!event.isCancelled()) {
            return Result.failure(EventAppError.EVENT_NOT_CANCELLED);
        }
        var updater = EventUpdater.updater(event)
                        .restore(messageToAttendees).update();
        if (updater.isFailure()) {
            return Result.failure(updater.getError());
        }
        var eventUpdated = updater.getValue();
        commandRepository.save(eventUpdated);
        return Result.success(eventUpdated);
    }

    @Override
    public Result<Event> restoreById(String eventId, String messageToAttendees) {
        var maybeEvent = queryRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        var event = maybeEvent.get();
        return restore(event, messageToAttendees);
    }
}
