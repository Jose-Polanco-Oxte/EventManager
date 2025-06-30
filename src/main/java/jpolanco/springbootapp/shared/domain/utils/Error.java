package jpolanco.springbootapp.shared.domain.utils;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class Error {
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

    public static final Error NONE = new Error(0, "No error") {
        // This is a placeholder for no error
    };

    public static final Error NULL_VALUE = new Error(400, "Invalid input") {
        // This is a placeholder for invalid input error
    };

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