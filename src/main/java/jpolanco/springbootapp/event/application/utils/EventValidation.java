package jpolanco.springbootapp.event.application.utils;

import jpolanco.springbootapp.event.application.errors.EventAppError;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.invitation.application.ports.output.InvitationRepository;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@RequiredArgsConstructor
@Component
public class EventValidation {
    private final EventRepository eventRepository;
    private final InvitationRepository invitationRepository;

    public boolean eventExists(String eventId) {
        return eventRepository.findById(eventId).isPresent();
    }
    public boolean eventBeforeThanNow(Instant date) {
        return date.isBefore(Instant.now());
    }

    public int creatorNotHaveOtherEventInSameSchedule(String creatorId, Instant date, long duration) {
        var events = eventRepository.findByCreatorId(creatorId);
        if (events.isEmpty()) {
            return 0; // No events found, so the creator can create a new one
        }
        for (Event event : events) {
            if (event.getSchedule().equals(date)) {
                return 1; // Creator already has an event at the same schedule
            }
            var startA = event.getSchedule();
            var endA = startA.plusSeconds(event.getDurationInSeconds());

            var endB = date.plusSeconds(duration);

            boolean collision = startA.isBefore(endB) && endA.isAfter(date);
            if (collision) {
                return 2; // Overlapping schedules
            }
        }
        return 0; // No conflicts found
    }

    public Result<Event> validate(String creatorId, Instant date, long duration, double latitude, double longitude) {
        if (eventRepository.sameScheduleExists(date, duration) && eventRepository.sameLocationExistsAndIsNotVirtual(latitude, longitude)) {
            return Result.failure(EventAppError.EVENT_LOCATION_AND_SCHEDULE_CONFLICT);
        }
        if (eventBeforeThanNow(date)) {
            return Result.failure(EventAppError.EVENT_SCHEDULE_IN_PAST);
        }
        return switch (creatorNotHaveOtherEventInSameSchedule(creatorId, date, duration)) {
            case 1 -> Result.failure(EventAppError.EVENT_SCHEDULE_CONFLICT);
            case 2 -> Result.failure(EventAppError.OVERLAPPING_EVENT_SCHEDULE);
            default -> // No conflicts found
                    Result.success();
        };
    }

    public void validateEventMaxInviteesOnChange(int newInvitees, String eventId) {
        if (invitationRepository.countInvitationsAcceptedByEventId(eventId) > newInvitees) {
            throw new IllegalArgumentException("The number of invitees cannot be less than the number of accepted invitations.");
        }
    }

    public Result<Event> validatePropietary(String creatorId, String eventId) {
        var event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return Result.failure(EventAppError.EVENT_NOT_FOUND);
        }
        if (!event.get().getCreatorId().equals(creatorId)) {
            return Result.failure(EventAppError.EVENT_NOT_BELONG_TO_USER);
        }
        return Result.success(event.get());
    }
}
