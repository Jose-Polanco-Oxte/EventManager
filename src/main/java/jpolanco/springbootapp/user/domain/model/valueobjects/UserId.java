package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.IdObject;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.errors.UserDomainError;

import java.util.UUID;

public class UserId extends IdObject {

    private UserId(String value) {
        super(value);
    }

    public static Result<UserId> create(String value) {
        if (value == null || value.isEmpty()) {
            return Result.failure(UserDomainError.NULL_VALUE.field("User id"));
        }
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return Result.failure(UserDomainError.INVALID_ID);
        }
        return Result.success(new UserId(value));
    }
}
