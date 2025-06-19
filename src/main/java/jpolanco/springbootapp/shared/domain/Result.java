package jpolanco.springbootapp.shared.domain;


import java.util.Objects;
import java.util.function.Function;

public class Result<T> {

    private T value;
    private boolean IsSuccess;
    private Error error;

    protected Result(boolean isSuccess, Error error) {
        if (isSuccess) {
            if (error != Error.NONE) {
                throw new IllegalArgumentException();
            } else {
                this.IsSuccess = true;
                this.error = error;
            }
        } else {
            if (error == Error.NONE) {
                throw new IllegalArgumentException();
            } else {
                this.IsSuccess = false;
                this.error = error;
            }
        }
    }

    protected Result(T value, boolean isSuccess, Error error) {
        this.value = value;
        this.IsSuccess = isSuccess;
        this.error = error;
    }

    public boolean isFailure() {
        return !IsSuccess;
    }

    public Error getError() {
        if (IsSuccess) {
            return Error.NONE;
        } else {
            return error;
        }
    }

    public int getErrorCode() {
        if (IsSuccess) {
            return Error.NONE.getCode();
        } else {
            return error.getCode();
        }
    }

    public String getMessage() {
        if (IsSuccess) {
            return "Success";
        } else {
            return error.message;
        }
    }

    public boolean isSuccess() {
        return IsSuccess;
    }

    public T getValue() {
        if (IsSuccess) {
            return value;
        } else {
            throw new IllegalStateException("Result has no value");
        }
    }

    public static <T> Result<T> success (T value) {
        return new Result<>(value, true, Error.NONE);
    }

    public static <T> Result<T> success () {
        return new Result<>(null, true, Error.NONE);
    }

    public static <T> Result<T> failure (Error error) {
        Objects.requireNonNull(error, "error must not be null");
        return new Result<>(null, false, error);
    }

    public static <T> Result<T> create (T value) {
        if (value == null) {
            return failure(Error.NULL_VALUE);
        } else {
            return success(value);
        }
    }

    public static <A, B> Result<B> bind (
            Result<A> r, Function<A, Result<B>> fn) {
        if (r.isFailure()) {
            return Result.failure(r.error);
        }
        return fn.apply(r.getValue());
    }
}
