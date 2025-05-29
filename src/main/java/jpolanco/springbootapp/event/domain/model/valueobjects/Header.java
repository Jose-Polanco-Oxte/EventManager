package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.event.domain.errors.HeaderError;
import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;
import lombok.Getter;

@Getter
public class Header {
    private String title;
    private String description;

    private Header(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static Result<Header> create(String title, String description) {
        if (title == null || title.isBlank()) {
            return Result.failure(Error.NULL_VALUE);
        }
        if (description == null || description.isBlank()) {
            return Result.failure(Error.NULL_VALUE);
        }
        if (title.length() > 100) {
            return Result.failure(HeaderError.TITLE_TOO_LONG);
        }
        if (description.length() > 500) {
            return Result.failure(HeaderError.DESCRIPTION_TOO_LONG);
        }
        if (title.length() < 3) {
            return Result.failure(HeaderError.TITLE_TOO_SHORT);
        }
        if (description.length() < 5) {
            return Result.failure(HeaderError.DESCRIPTION_TOO_SHORT);
        }
        return Result.success(new Header(title, description));
    }
}
