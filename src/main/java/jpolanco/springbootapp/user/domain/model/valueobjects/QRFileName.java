package jpolanco.springbootapp.user.domain.model.valueobjects;

import jpolanco.springbootapp.shared.domain.Error;
import jpolanco.springbootapp.shared.domain.Result;

public class QRFileName {
    private final String fileName;

    private QRFileName(String value) {
        this.fileName = value;
    }

    public static Result<QRFileName> create(String value) {
        if (value == null || value.isEmpty()) {
            return Result.failure(Error.NULL_VALUE.field("QR file name"));
        }
        return Result.success(new QRFileName(value));
    }

    public String getValue() {
        return fileName;
    }

}
