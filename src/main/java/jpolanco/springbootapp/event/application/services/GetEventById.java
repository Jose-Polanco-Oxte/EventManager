package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.GetEventByIdUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetEventById implements GetEventByIdUC {

    private final EventRepository eventRepository;

    @Override
    public Result<Event> getById(String eventId) {
        var event = eventRepository.findById(eventId);
        return event.map(Result::success).orElseGet(() -> Result.failure(EventAppError.EVENT_NOT_FOUND));
    }
}