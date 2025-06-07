package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.GetAllEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllEvents implements GetAllEventsUC {

    private final EventRepository eventRepository;

    @Override
    public Result<List<Event>> getAllEvents(String userId) {
        List<Event> events = eventRepository.findAll();
        if (events.isEmpty()) {
            return Result.failure(EventAppError.NO_EVENTS_AVAILABLE);
        }
        return Result.success(events);
    }
}
