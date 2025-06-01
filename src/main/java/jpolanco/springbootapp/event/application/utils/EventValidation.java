package jpolanco.springbootapp.event.application.utils;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@RequiredArgsConstructor
@Component
public class EventValidation {
    private final EventRepository eventRepository;

    public boolean eventExists(String eventId) {
        return eventRepository.findById(eventId).isPresent();
    }

    public Result<Event> validate(Instant date, long duration, double latitude, double longitude) {
        if (eventRepository.sameScheduleExists(date, duration) && eventRepository.sameLocationExists(latitude, longitude)) {
            return Result.failure(EventAppError.EVENT_LOCATION_AND_SCHEDULE_CONFLICT);
        }
        return Result.success();
    }
}
