package jpolanco.springbootapp.shared.utils;

import java.util.NoSuchElementException;

public abstract class Either<L, R> {

    private Either() {}

    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    public abstract boolean isLeft();
    public abstract boolean isRight();

    public abstract L getLeft();
    public abstract R getRight();

    private static class Left<L, R> extends Either<L, R> {
        private final L value;

        private Left(L value) {
            this.value = value;
        }

        public boolean isLeft() { return true; }
        public boolean isRight() { return false; }
        public L getLeft() { return value; }
        public R getRight() { throw new NoSuchElementException("No right value"); }
    }

    private static class Right<L, R> extends Either<L, R> {
        private final R value;

        private Right(R value) {
            this.value = value;
        }

        public boolean isLeft() { return false; }
        public boolean isRight() { return true; }
        public L getLeft() { throw new NoSuchElementException("No left value"); }
        public R getRight() { return value; }
    }
}