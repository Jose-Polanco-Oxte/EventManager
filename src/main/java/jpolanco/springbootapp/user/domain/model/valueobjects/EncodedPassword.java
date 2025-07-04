package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.utils.results.Result;
import jpolanco.springbootapp.shared.utils.Validators;

import java.util.Optional;

public class EncodedPassword {
    private final String value;

    private EncodedPassword(String value) {
        this.value = value;
    }

    public static Result<EncodedPassword> create(String value) {
        Optional<DomainError> error = Validators.notBlank("EncodedPassword", value);
        if (error.isPresent()) return Result.failure(error.get());

        error = Validators.minLength("EncodedPassword", value, 6);
        if (error.isPresent()) return Result.failure(error.get());

        return Result.success(new EncodedPassword(value));
    }

    protected static EncodedPassword loadUnchecked(String value) {
        return new EncodedPassword(value);
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

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
