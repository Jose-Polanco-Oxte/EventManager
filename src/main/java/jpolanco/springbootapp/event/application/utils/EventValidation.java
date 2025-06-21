package jpolanco.springbootapp.event.application.utils;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class EventValidation {
    private final EventQueryRepository queryRepository;

    private int creatorNotHaveOtherEventInSameSchedule(String creatorId, Instant date, long duration) {
        var events = queryRepository.findFirstConflictingEvent(date, date.plusSeconds(duration), creatorId);
        if (events.isPresent()) {
            var event = events.get();
            if (event.getSchedule().equals(date)) {
                return 1;
            }
            var startA = event.getSchedule();
            var endA = startA.plusSeconds(event.getDurationInSeconds());
            var endB = date.plusSeconds(duration);
            boolean collision = startA.isBefore(endB) && endA.isAfter(date);
            if (collision) {
                return 2;
            }
        }
        return 0; // No conflicts found
    }

    public Result<Void> validate(String creatorId, Instant date, long duration, double latitude, double longitude) {
        if (queryRepository.sameScheduleExists(date, duration) && queryRepository.sameLocationExistsAndIsNotVirtual(latitude, longitude)) {
            return Result.failure(EventAppError.EVENT_LOCATION_AND_SCHEDULE_CONFLICT);
        }
        if (date.isBefore(Instant.now())) {
            return Result.failure(EventAppError.EVENT_SCHEDULE_IN_PAST);
        }
        return switch (creatorNotHaveOtherEventInSameSchedule(creatorId, date, duration)) {
            case 1 -> Result.failure(EventAppError.EVENT_SCHEDULE_CONFLICT);
            case 2 -> Result.failure(EventAppError.OVERLAPPING_EVENT_SCHEDULE);
            default -> // No conflicts found
                    Result.success();
        };
    }
}
