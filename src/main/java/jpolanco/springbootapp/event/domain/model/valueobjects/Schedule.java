package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class Schedule {
    private String dateUTC;

    private Schedule(String value) {
        this.dateUTC = value;
    }

    public static Result<Schedule> create(String dateUTC) {
        if (dateUTC == null || dateUTC.isBlank()) {
            return Result.failure(Error.NULL_VALUE);
        }
        try {
            Instant.parse(dateUTC);
        } catch (DateTimeParseException e) {
            return Result.failure(new Error("INVALID_DATE", "The provided date is invalid."));
        }
        return Result.success(new Schedule(dateUTC));
    }

    public String getValue() {
        return dateUTC;
    }
}
