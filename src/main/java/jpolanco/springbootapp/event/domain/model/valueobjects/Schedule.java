package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.event.domain.errors.EventDomainError;
import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;

import java.time.Instant;

public class Schedule {
    private Instant dateUTC;

    private Schedule(Instant value) {
        this.dateUTC = value;
    }

    public static Result<Schedule> create(Instant dateUTC) {
        if (dateUTC == null) {
            return Result.failure(Error.NULL_VALUE.field("DateUTC"));
        }
        if (dateUTC.isBefore(Instant.now())) {
            return Result.failure(EventDomainError.PAST_EVENT);
        }
        return Result.success(new Schedule(dateUTC));
    }

    public Instant getValue() {
        return dateUTC;
    }
}
