package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.event.domain.errors.EventDomainError;
import jpolanco.springbootapp.shared.domain.Result;

public class Attendees {
    private long maxAttendees;
    private long currentAttendees;

    private Attendees(long maxAttendees, long currentAttendees) {
        this.maxAttendees = maxAttendees;
        this.currentAttendees = currentAttendees;
    }

    public static Result<Attendees> create(long maxAttendees, long currentAttendees) {
        if (maxAttendees <= 0) {
            return Result.failure(EventDomainError.INVALID_NUMBER_OF_ATTENDEES);
        }
        if (currentAttendees > maxAttendees) {
            return Result.failure(EventDomainError.MAX_ATTENDEES_EXCEEDED);
        }
        return Result.success(new Attendees(maxAttendees, currentAttendees));
    }

    private Result<Void> validate(long maxAttendees, long currentAttendees) {
        if (maxAttendees <= 0) {
            return Result.failure(EventDomainError.INVALID_NUMBER_OF_ATTENDEES);
        }
        if (currentAttendees > maxAttendees) {
            return Result.failure(EventDomainError.MAX_ATTENDEES_EXCEEDED);
        }
        return Result.success();
    }


    public long getMaxAttendees() {
        return maxAttendees;
    }

    public long getCurrentAttendees() {
        return currentAttendees;
    }

    public void setMaxAttendees(long maxAttendees) {
        Result<Void> validationResult = validate(maxAttendees, this.currentAttendees);
        this.maxAttendees = maxAttendees;
    }

    public Result<Void> addAttendee() {
        Result<Void> validationResult = validate(this.maxAttendees, this.currentAttendees + 1);
        if (validationResult.isFailure()) {
            return validationResult;
        }
        this.currentAttendees++;
        return Result.success();
    }
}
