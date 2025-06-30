package jpolanco.springbootapp.shared.utils;

import jpolanco.springbootapp.shared.domain.Report;
import jpolanco.springbootapp.shared.domain.utils.DomainError;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SuperResult<S, F extends Report> {
    private final Either<S, F> result;

    private SuperResult(Either<S, F> result) {
        this.result = result;
    }

    public static <S, F extends Report> SuperResult<S, F> success(S successValue) {
        return new SuperResult<>(Either.left(successValue));
    }

    public static <S, F extends Report> SuperResult<S, F> failure(F failureValue) {
        return new SuperResult<>(Either.right(failureValue));
    }

    public boolean isSuccess() {
        return result.isLeft();
    }

    public String getErrorMessage() {
        if (isFailure()) {
            return getFailure().getErrors().stream()
                    .map(e -> e.getField() + " " + e.getMessage() + " " +
                            ((e instanceof DomainError p && p.getDetails() != null) ? p.getDetails() : ""))
                    .reduce("", (a, b) -> a + " " + b).trim();
        } else {
            return "";
        }
    }

    public boolean isFailure() {
        return result.isRight();
    }

    public S getSuccess() {
        if (isSuccess()) {
            return result.getLeft();
        } else {
            throw new NoSuchElementException();
        }
    }

    public F getFailure() {
        if (isFailure()) {
            return result.getRight();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public String toString() {
        return "SuperResult{" +
                "result=" + result +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuperResult<?, ?> other = (SuperResult<?, ?>) o;
        return Objects.equals(result, other.result);
    }

    @Override
    public int hashCode() {
        return result.hashCode();
    }
}
