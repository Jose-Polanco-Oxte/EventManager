package jpolanco.springbootapp.event.application.services.derived;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.base.RestoreEventUC;
import jpolanco.springbootapp.event.application.uc.derived.RestoreOwnEventUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestoreOwnEvent implements RestoreOwnEventUC {
    private final EventQueryRepository queryRepository;
    private final RestoreEventUC restoreEventUC;

    @Override
    public Result<Event> restore(String eventId, String userId, String messageToAttendees) {
        var maybeEvent = queryRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        var event = maybeEvent.get();
        if (!event.getCreatorId().equals(userId)) {
            return Result.failure(EventAppError.EVENT_NOT_BELONG_TO_USER);
        }
        return restoreEventUC.restore(event, messageToAttendees);
    }
}
