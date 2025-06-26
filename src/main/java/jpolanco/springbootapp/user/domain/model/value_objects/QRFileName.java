package jpolanco.springbootapp.user.domain.model.value_objects;

import jpolanco.springbootapp.shared.domain.DomainError;
import jpolanco.springbootapp.shared.domain.Result;

public class QRFileName {
    private final String value;

    private QRFileName(String value) {
        this.value = value;
    }

    public static Result<QRFileName> create(String value) {
        if (value == null || value.isBlank()) {
            return Result.failure(DomainError.NULL_VALUE
                    .withField("QRFileName"));
        }
        if (value.contains(".")) {
            return Result.failure(DomainError.INVALID_FORMAT
                    .withDetails("QR file name cannot contain a dot")
                    .withField("QRFileName"));
        }
        return Result.success(new QRFileName(value.trim().replaceAll(" ", "_")));
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
}
