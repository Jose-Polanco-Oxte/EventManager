package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.errors.UserDomainError;

public class EncodedPassword {
    private final String value;

    private EncodedPassword(String value) {
        this.value = value;
    }

    public static Result<EncodedPassword> create(String value) {
        if (value == null || value.isBlank()) {
            return Result.failure(UserDomainError.NULL_VALUE.field("EncodedPassword"));
        }
        if (value.length() < 6) {
            return Result.failure(UserDomainError.INVALID_ENCODED_PASSWORD_LENGTH);
        }
        return Result.success(new EncodedPassword(value));
    }

    public String getValue() {
        return value;
    }
}
