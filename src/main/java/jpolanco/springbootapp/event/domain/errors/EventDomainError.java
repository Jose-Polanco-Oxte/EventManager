package jpolanco.springbootapp.event.domain.errors;

import jpolanco.springbootapp.shared.domain.utils.Error;

public class EventDomainError extends Error {
    
    public EventDomainError(int code, String message) {super(code, message);}

    public static final EventDomainError TITLE_TOO_LONG =
            new EventDomainError(400, "Title cannot exceed 100 characters.");
    public static final EventDomainError DESCRIPTION_TOO_LONG =
            new EventDomainError(400, "Description cannot exceed 500 characters.");
    public static final EventDomainError TITLE_TOO_SHORT =
            new EventDomainError(400, "Title must be at least 3 characters long.");
    public static final EventDomainError DESCRIPTION_TOO_SHORT =
            new EventDomainError(400, "Description must be at least 5 characters long.");
    public static final EventDomainError LOCATION_FORMAT_ERROR =
            new EventDomainError(422, "Location format is invalid.");
    public static final EventDomainError INVALID_CATEGORY =
            new EventDomainError(422, "Category cannot be empty.");
    public static final EventDomainError INVALID_ID =
            new EventDomainError(400, "Event ID is invalid.");
    public static final EventDomainError INVALID_DATE =
            new EventDomainError(422, "Event date cannot be in the past.");
    public static final EventDomainError PAST_EVENT =
            new EventDomainError(422, "Cannot create or update an event in the past.");
    public static final EventDomainError INVALID_ROLE =
            new EventDomainError(422, "Format invoke role is invalid.");
    public static final EventDomainError INVALID_NUMBER_OF_ATTENDEES =
            new EventDomainError(422, "Number invoke attendees cannot be negative or zero.");
    public static final EventDomainError MAX_ATTENDEES_EXCEEDED =
            new EventDomainError(409, "Cannot exceed maximum number invoke attendees.");
    public static final EventDomainError INVALID_DURATION =
            new EventDomainError(422, "Event duration cannot be negative or zero.");
    public static final EventDomainError DURATION_TOO_LONG =
            new EventDomainError(422, "Event duration cannot exceed 24 hours.");
    public static final EventDomainError DURATION_TOO_SHORT =
            new EventDomainError(422, "Event duration must be at least 10 minutes.");
    public static final EventDomainError CATEGORIES_EMPTY =
            new EventDomainError(400, "Event must have at least one category.");
    public static final EventDomainError STAFF_NOT_FOUND =
            new EventDomainError(404, "Staff member not found while removing from event.");
}