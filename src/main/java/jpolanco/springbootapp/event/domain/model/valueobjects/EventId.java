package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.event.domain.errors.EventDomainError;
import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.IdObject;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.UUID;

public class EventId extends IdObject {
    private EventId(String value) {
        super(value);
    }

    public static Result<EventId> create(String value) {
        if (value == null || value.isEmpty()) {
            return Result.failure(Error.NULL_VALUE.field("Event id"));
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return Result.failure(EventDomainError.INVALID_ID);
        }
        return Result.success(new EventId(value));
    }
}
