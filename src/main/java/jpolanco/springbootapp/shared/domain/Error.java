package jpolanco.springbootapp.shared.domain;

import org.apache.logging.log4j.util.Strings;

public class Error {
    final int code;
    final String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Error() {
        this.code = 0;
        this.message = Strings.EMPTY;
    }

    public static Error NONE = new Error( -1, Strings.EMPTY);
    public static Error NULL_VALUE = new Error(500, "cannot be null or empty");
    public Error field(String fieldName) {
        return new Error(500, fieldName + " cannot be null or empty");
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}