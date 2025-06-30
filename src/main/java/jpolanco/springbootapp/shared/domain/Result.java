package jpolanco.springbootapp.shared.domain;


import jpolanco.springbootapp.shared.domain.utils.DomainError;
import jpolanco.springbootapp.shared.domain.utils.Error;

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

    public boolean equalTypes(Error error) {
        if (IsSuccess) {
            return error == Error.NONE;
        } else {
            return this.error.equals(error);
        }
    }

    public boolean dataIsNull() {
        if (IsSuccess) {
            return value == null;
        } else {
            return false; // If it's a failure, we don't check the value
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
            return error.getMessage();
        }
    }

    public String getField() {
        if (IsSuccess) {
            return null;
        } else {
            return error.getField();
        }
    }

    public String getDetails() {
        if (IsSuccess) {
            return null;
        } else if (error instanceof DomainError) {
            return ((DomainError) error).getDetails();
        } else {
            return null;
        }
    }

    public DomainError getDomainError() {
        if (IsSuccess) {
            return null;
        } else if (error instanceof DomainError) {
            return (DomainError) error;
        } else {
            return null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return Objects.equals(value, result.value) &&
                Objects.equals(this.error, result.error) &&
                this.IsSuccess == result.IsSuccess;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, error, IsSuccess);
    }

    @Override
    public String toString() {
        if (IsSuccess) {
            return "Result{value=" + value + ", IsSuccess=" + IsSuccess + ", error=" + error + '}';
        } else {
            return "Result{IsSuccess=" + IsSuccess + ", error=" + error + '}';
        }
    }
}
