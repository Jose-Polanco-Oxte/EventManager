package jpolanco.applicationcore.shared.domain.utils.primitives.utils;

/**
 * A generic Pair class that holds two related objects of types A and B.
 *
 * @param <A> the type of the first element
 * @param <B> the type of the second element
 *            <p> Example usage:
 *            *           Pair<String, Integer> pair = Pair.of("age", 30);
 *            *           String key = pair.getFirst(); // "age"
 *            *           Integer value = pair.getSecond(); // 30
 */
public class Pair<A, B> {
    private final A first;
    private final B second;

    private Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    public A getFirst() {
        if (first == null) {
            throw new IllegalStateException("First element is null");
        }
        return first;
    }

    public B getSecond() {
        if (second == null) {
            throw new IllegalStateException("Second element is null");
        }
        return second;
    }

    @Override
    public String toString() {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }
}
