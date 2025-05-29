package jpolanco.springbootapp.event.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;

public class PictureFileName {
    private final String value;

    private PictureFileName(String value) {
        this.value = value;
    }

    public static Result<PictureFileName> create(String value) {
        if (value == null || value.isEmpty()) {
            return Result.failure(Error.NULL_VALUE);
        }
        return Result.success(new PictureFileName(value));
    }

    public String getValue() {
        return value;
    }
}
