package jpolanco.springbootapp.event.application.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class EventAppError extends Error {
    public EventAppError(String code, String message) {
        super(code, message);
    }

    public static EventAppError EVENT_SCHEDULE_CONFLICT = new EventAppError("EVENT_SCHEDULE_CONFLICT", "An event with the same schedule already exists");
    public static EventAppError EVENT_ALREADY_EXISTS = new EventAppError("EVENT_ALREADY_EXISTS", "An event with the same ID already exists");
    public static EventAppError EVENT_LOCATION_AND_SCHEDULE_CONFLICT = new EventAppError("EVENT_LOCATION_AND_SCHEDULE_CONFLICT", "An event with the same location and schedule already exists");
}
