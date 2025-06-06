package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.IdObject;
import jpolanco.springbootapp.shared.domain.Result;

import java.util.UUID;

public class UserId extends IdObject {

    private UserId(String value) {
        super(value);
    }

    public static Result<UserId> create(String value) {
        if (value == null || value.isEmpty()) {
            return Result.failure(Error.NULL_VALUE.field("User id"));
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return Result.failure(new Error("INVALID_UUID", "The provided UUID is invalid."));
        }
        return Result.success(new UserId(value));
    }
}
