package jpolanco.applicationcore.user.domain.model;

import jpolanco.applicationcore.shared.domain.utils.Validators;
import jpolanco.applicationcore.shared.domain.utils.abstracterrors.DomainError;
import jpolanco.applicationcore.shared.domain.utils.primitives.results.Result;

public class EncodedPassword {

    private final String value;

    private EncodedPassword(String encodedPassword) {
        this.value = encodedPassword;
    }

    public static Result<EncodedPassword, DomainError> create(String value) {
        if (value == null || value.isBlank()) {
            return Validators.EMPTY_VALUE("encodedPassword");
        }

        if (value.length() < 6) {
            return Validators.TOO_SHORT("encodedPassword", value, 6);
        }

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
