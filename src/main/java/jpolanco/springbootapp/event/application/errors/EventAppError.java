package jpolanco.springbootapp.event.application.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class EventAppError extends Error {
    public EventAppError(String code, String message) {
        super(code, message);
    }

    public static EventAppError EVENT_SCHEDULE_CONFLICT = new EventAppError("EVENT_SCHEDULE_CONFLICT", "An event with the same schedule already exists");
    public static EventAppError EVENT_LOCATION_AND_SCHEDULE_CONFLICT = new EventAppError("EVENT_LOCATION_AND_SCHEDULE_CONFLICT", "you cannot create an event with the same location and schedule as another event");
    public static EventAppError EVENT_SCHEDULE_IN_PAST = new EventAppError("EVENT_SCHEDULE_IN_PAST", "The event schedule cannot be in the past");
    public static EventAppError OVERLAPPING_EVENT_SCHEDULE = new EventAppError("OVERLAPPING_EVENT_SCHEDULE", "The event schedule overlaps with another event's schedule");
}
