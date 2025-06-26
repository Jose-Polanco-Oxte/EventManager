package jpolanco.springbootapp.shared.domain;

import java.util.Objects;

public class DomainError extends Error{
    private String details;
    public static final DomainError NULL_VALUE = new DomainError(400, "value cannot be null or empty");
    public static final DomainError TOO_LONG = new DomainError(400, "value is too long");
    public static final DomainError TOO_SHORT = new DomainError(400, "value is too short");
    public static final DomainError INVALID_PARAMETER = new DomainError(400, "invalid parameter");
    public static final DomainError INVALID_FORMAT = new DomainError(400, "invalid format");

    public DomainError(int code, String message) {
        super(code, message);
        this.details = null;
    }

    public DomainError(int code, String message, String field) {
        super(code, message, field);
        this.details = null;
    }

    public DomainError(int code, String message, String field, String details) {
        super(code, message, field);
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    public DomainError withDetails(String details) {
        return new DomainError(getCode(), getMessage(), getField(), details);
    }

    public DomainError withField(String fieldName) {
        return new DomainError(getCode(), getMessage(), fieldName, details);
    }

    public DomainError withCode(int code) {
        return new DomainError(code, getMessage(), getField(), details);
    }

    public DomainError withMessage(String message) {
        return new DomainError(getCode(), message, getField(), details);
    }

    public static DomainError of(int code, String message) {
        return new DomainError(code, message);
    }

    public DomainError getError() {
        return new DomainError(getCode(), getMessage(), getField(), details);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainError that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(details, that.details)
                && Objects.equals(getField(), that.getField())
                && Objects.equals(getMessage(), that.getMessage())
                && getCode() == that.getCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), details);
    }
}
