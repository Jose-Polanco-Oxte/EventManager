package jpolanco.applicationcore.shared.domain.utils.abstracterrors;

import java.util.Objects;

/**
 * Abstract class representing an error with a code, message, and optional field.
 * <p>{@code NONE} represents no error.
 * <p>{@code NULL_VALUE} represents an invalid input error.
 * <p>{@link Error} is the error code.
 */
public abstract class Error {
    public static final Error NONE = new Error(0, "No error") {
        // This is a placeholder for no error
    };
    public static final Error NULL_VALUE = new Error(400, "Invalid input") {
        // This is a placeholder for invalid input error
    };

    private final int code;

    private final String message;

    private final String field;

    public Error(int code, String message) {
        this(code, message, null);
    }

    public Error(int code, String message, String field) {
        this.code = code;
        this.message = message;
        this.field = field;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Error other)) return false;
        return code == other.code &&
                message.equals(other.message) &&
                Objects.equals(field, other.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, field);
    }

    @Override
    public String toString() {
        return "Error{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}