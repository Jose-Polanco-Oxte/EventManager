package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.IdObject;
import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.errors.UserDomainError;

import java.util.UUID;
import java.util.regex.Pattern;

public class UserId extends IdObject {

    private UserId(String value) {
        super(value);
    }
    private final static Pattern UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$");

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserId)) return false;
        UserId userId = (UserId) o;
        return this.getValue().equals(userId.getValue());
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }
}
