package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.Set;

public class Status {
    private final String value;
    private final static Set<String> validValues = Set.of("ACTIVE", "INACTIVE", "SUSPENDED");

    private Status(String value) {
        this.value = value;
    }

    public static Result<Status> create(String value) {
        if (value == null || value.isBlank()) {
            return Result.failure(Error.NULL_VALUE.field("Status"));
        }
        if (!validValues.contains(value)) {
            return Result.failure(new Error("StatusNotValid", "Status is not valid"));
        }
        return Result.success(new Status(value));
    }

    public String getValue() {
        return value;
    }
}
