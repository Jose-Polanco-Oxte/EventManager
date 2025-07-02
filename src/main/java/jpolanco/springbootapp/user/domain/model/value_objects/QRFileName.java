package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.Result;
import jpolanco.springbootapp.shared.utils.Validators;

public class QRFileName {
    private final String value;

    private QRFileName(String value) {
        this.value = value;
    }

    public static Result<QRFileName> create(String value) {
        var error = Validators.notBlank("QRFileName", value);
        if (error.isPresent()) return Result.failure(error.get());

        error = Validators.mustNotContain("QRFileName", value, ".");
        if (error.isPresent()) return Result.failure(error.get());

        return Result.success(new QRFileName(value.trim().replaceAll(" ", "_")));
    }

    protected static QRFileName loadUnchecked(String value) {
        return new QRFileName(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QRFileName other)) return false;
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
