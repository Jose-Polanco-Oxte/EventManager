package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.user.domain.errors.UserDomainError;

public class EncodedPassword {
    private final String value;

    private EncodedPassword(String value) {
        this.value = value;
    }

    public static Result<EncodedPassword> create(String value) {
        if (value == null || value.isBlank()) {
            Result.failure(UserDomainError.NULL_VALUE.field("EncodedPassword"));
        }
        return Result.success(new EncodedPassword(value));
    }

    public String getValue() {
        return value;
    }
}
