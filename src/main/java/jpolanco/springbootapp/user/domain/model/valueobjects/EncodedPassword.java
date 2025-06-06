package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;

public class EncodedPassword {
    private final String value;

    private EncodedPassword(String value) {
        this.value = value;
    }

    public static Result<EncodedPassword> create(String value) {
        if (value == null || value.isBlank()) {
            Result.failure(Error.NULL_VALUE.field("EncodedPassword"));
        }
        return Result.success(new EncodedPassword(value));
    }

    public String getValue() {
        return value;
    }
}
