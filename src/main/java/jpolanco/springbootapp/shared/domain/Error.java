package jpolanco.springbootapp.shared.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Error {
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

    public static Error NONE = new Error(0, "No error");
    public static Error NULL_VALUE = new Error(400, "Value cannot be null");

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
}