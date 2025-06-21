package jpolanco.springbootapp.event.application.services.derived;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventCommandRepository;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.base.CancelEventUC;
import jpolanco.springbootapp.event.application.uc.derived.CancelOwnEventUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelOwnEvent implements CancelOwnEventUC {
    private final EventQueryRepository queryRepository;
    private final EventCommandRepository commandRepository;
    private final CancelEventUC cancelEventUC;

    @Override
    public Result<Event> cancel(String eventId, String userId, String reason) {
        var maybeEvent = queryRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        if (!maybeEvent.get().getCreatorId().equals(userId)) {
            return Result.failure(EventAppError.EVENT_NOT_BELONG_TO_USER);
        }
        var event = maybeEvent.get();
        var result = cancelEventUC.cancel(event, reason);
        if (result.isFailure()) {
            return Result.failure(result.getError());
        }
        return Result.success(event);
    }
}
