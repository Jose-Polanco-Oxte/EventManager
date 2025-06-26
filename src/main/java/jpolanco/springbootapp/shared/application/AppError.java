package jpolanco.springbootapp.shared.application;

import jpolanco.springbootapp.shared.domain.Error;

import java.util.Objects;

public class AppError extends Error {
    public static final AppError RESOURCE_NOT_FOUND = new AppError(404, "resource not found");
    public static final AppError RESOURCE_ALREADY_EXISTS = new AppError(409, "resource already exists");
    public static final AppError CONFLICT = new AppError(409, "conflict");
    public static final AppError UNAUTHORIZED = new AppError(401, "unauthorized");
    public static final AppError INVALID_OPERATION = new AppError(405, "invalid operation");
    public static final AppError FORBIDDEN = new AppError(403, "forbidden");
    public static final AppError BAD_REQUEST = new AppError(400, "bad request");
    public static final AppError INTERNAL_SERVER_ERROR = new AppError(500, "internal server AppError");
    public static final AppError UNPROCESSABLE_ENTITY = new AppError(422, "unprocessable entity");
    public static final AppError SERVICE_UNAVAILABLE = new AppError(503, "service unavailable");
    public static final AppError TOO_MANY_REQUESTS = new AppError(429, "too many requests");
    public static final AppError NOT_IMPLEMENTED = new AppError(501, "not implemented");

    public AppError(int code, String message) {
        super(code, message);
    }

    public AppError(int code, String message, String field) {
        super(code, message, field);
    }

    public AppError withField(String fieldName) {
        return new AppError(getCode(), getMessage(), fieldName);
    }

    public AppError concatMessage(String fieldName) {
        return new AppError(getCode(), getMessage() + " " + fieldName);
    }

    public static <T> AppError idNotFound(T id, String fieldName) {
        return of(404, "id " + id + " not found")
                .withField(fieldName);
    }

    public AppError withCode(int code) {
        return new AppError(code, getMessage(), getField());
    }

    public AppError withMessage(String message) {
        return new AppError(getCode(), message, getField());
    }

    public static AppError of(int code, String message) {
        return new AppError(code, message);
    }

    public AppError getError() {
        return new AppError(getCode(), getMessage(), getField());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppError that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getField(), that.getField())
                && Objects.equals(getMessage(), that.getMessage())
                && getCode() == that.getCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
