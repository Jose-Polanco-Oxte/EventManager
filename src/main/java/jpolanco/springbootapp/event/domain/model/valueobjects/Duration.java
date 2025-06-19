package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.event.domain.errors.EventDomainError;
import jpolanco.springbootapp.shared.domain.Result;

public class Duration {
    private final long durationInSeconds;

    private Duration(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public static Result<Duration> create(long durationInSeconds) {
        if (durationInSeconds <= 0) {
            return Result.failure(EventDomainError.INVALID_DURATION);
        } else if (durationInSeconds > 24 * 60 * 60) { // More than 24 hours
            return Result.failure(EventDomainError.DURATION_TOO_LONG);
        } else if (durationInSeconds < 60 * 10) { // Less than 10 minutes
            return Result.failure(EventDomainError.DURATION_TOO_SHORT);
        }
        return Result.success(new Duration(durationInSeconds));
    }

    public long getValue() {
        return durationInSeconds;
    }
}
