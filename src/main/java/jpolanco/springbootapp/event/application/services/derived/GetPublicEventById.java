package jpolanco.springbootapp.event.application.services.derived;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.uc.base.GetEventByIdUC;
import jpolanco.springbootapp.event.application.uc.derived.GetPublicEventByIdUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPublicEventById implements GetPublicEventByIdUC {
    private final GetEventByIdUC getEventByIdUC;

    @Override
    public Result<Event> get(String eventId) {
        var maybeEvent = getEventByIdUC.get(eventId);
        if (maybeEvent.isFailure()) {
            return Result.failure(maybeEvent.getError());
        }
        var event = maybeEvent.getValue();
        if (event.isPrivate()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND_PUBLIC);
        }
        return Result.success(event);
    }
}
