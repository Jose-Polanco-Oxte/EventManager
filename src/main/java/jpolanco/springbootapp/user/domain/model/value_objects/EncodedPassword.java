package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.DomainError;
import jpolanco.springbootapp.shared.domain.Result;

public class EncodedPassword {
    private final String value;

    private EncodedPassword(String value) {
        this.value = value;
    }

    public static Result<EncodedPassword> create(String value) {
        if (value == null || value.isBlank()) {
            return Result.failure(DomainError.NULL_VALUE
                    .withField("EncodedPassword"));
        }
        if (value.length() < 6) {
            return Result.failure(DomainError.TOO_SHORT
                    .withDetails("Encoded password must be at least 6 characters long")
                    .withField("EncodedPassword"));
        }
        return Result.success(new EncodedPassword(value));
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EncodedPassword other)) return false;
        return value.equals(other.value);
    }
}
