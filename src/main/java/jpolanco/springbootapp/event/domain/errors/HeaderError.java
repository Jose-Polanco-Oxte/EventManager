package jpolanco.springbootapp.event.domain.errors;

import jpolanco.springbootapp.shared.domain.Error;

public class HeaderError extends Error {
    public HeaderError(String code, String message) {
        super(code, message);
    }

    public static HeaderError TITLE_TOO_LONG = new HeaderError("TITLE_TOO_LONG", "Title cannot exceed 100 characters.");
    public static HeaderError DESCRIPTION_TOO_LONG = new HeaderError("DESCRIPTION_TOO_LONG", "Description cannot exceed 500 characters.");
    public static HeaderError TITLE_TOO_SHORT = new HeaderError("TITLE_TOO_SHORT", "Title must be at least 3 characters long.");
    public static HeaderError DESCRIPTION_TOO_SHORT = new HeaderError("DESCRIPTION_TOO_SHORT", "Description must be at least 5 characters long.");
}
