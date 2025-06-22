package jpolanco.springbootapp.event.application.services.base;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.base.GetEventByIdUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetEventById implements GetEventByIdUC {
    private final EventQueryRepository queryRepository;

    @Override
    public Result<Event> get(String eventId) {
        var event = queryRepository.findById(eventId);
        return event.map(Result::success).orElseGet(() -> Result.failure(EventAppError.EVENT_NOT_FOUND));
    }
}