package jpolanco.springbootapp.event.application.errors;

import jpolanco.springbootapp.shared.domain.utils.Error;

public class EventAppError extends Error {
    public EventAppError(int code, String message) {
        super(code, message);
    }

    public static final EventAppError EVENT_SCHEDULE_CONFLICT =
            new EventAppError(409, "An event with the same schedule already exists");

    public static final EventAppError EVENT_LOCATION_AND_SCHEDULE_CONFLICT =
            new EventAppError(409, "You cannot create an event with the same location and schedule as another event");

    public static final EventAppError EVENT_SCHEDULE_IN_PAST =
            new EventAppError(422, "The event schedule cannot be in the past");

    public static final EventAppError EVENT_NOT_FOUND =
            new EventAppError(404, "Event not found");

    public static final EventAppError EVENT_NOT_BELONG_TO_USER =
            new EventAppError(403, "The event does not belong to the user");

    public static final EventAppError OVERLAPPING_EVENT_SCHEDULE =
            new EventAppError(409, "The event schedule overlaps with another event");

    public static final EventAppError IMAGE_STORAGE_ERROR =
            new EventAppError(500, "An error occurred while storing the event image");

    public static final EventAppError IMAGE_DELETE_ERROR =
            new EventAppError(500, "An error occurred while deleting the event image");

    public static final EventAppError EVENT_NOT_CANCELLED_OR_COMPLETED =
            new EventAppError(409, "The event must be cancelled or completed to be deleted");

    public static final EventAppError EVENT_ALREADY_CANCELLED =
            new EventAppError(409, "The event is already cancelled");

    public static final EventAppError MAX_ATTENDEES_LESS_THAN_CURRENT =
            new EventAppError(422, "The number invoke invitees cannot be less than the number invoke accepted invitations");

    public static final EventAppError EVENT_NOT_FOUND_PUBLIC =
            new EventAppError(404, "The event was not found or is private and cannot be accessed publicly");

    public static final EventAppError EVENT_NOT_CANCELLED =
            new EventAppError(409, "The event is not cancelled and cannot be restored");
}
