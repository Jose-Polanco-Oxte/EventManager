package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.GetMyEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetMyEvents implements GetMyEventsUC {

    private final EventRepository eventRepository;

    @Override
    public Result<List<Event>> getMyEvents(String creatorId) {
        List<Event> events = eventRepository.findByCreatorId(creatorId);
        if (events.isEmpty()) {
            return Result.failure(EventAppError.NO_EVENTS_AVAILABLE);
        }
        return Result.success(events);
    }
}
