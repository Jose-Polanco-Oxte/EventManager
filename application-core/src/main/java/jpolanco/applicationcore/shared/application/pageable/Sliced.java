package jpolanco.applicationcore.shared.application.pageable;

import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.function.Function;

/**
 * Sliced class for pagination with simple next page indication.
 *
 * @param <T> the type of content in the slice.
 *            <p>
 *            {@link #hasNext()} indicates if there is a next page.
 *            {@link #getContent()} returns the list of items in the current slice.
 *            </p>
 */
public class Sliced<T> {

    private final List<T> content;

    private final boolean hasNext;

    public Sliced(List<T> content, boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }

    // Method to map the content of the Sliced to another type.
    public <R> Sliced<R> map(Function<T, R> mapper) {
        List<R> mappedContent = Streamable.of(content).map(mapper).toList();
        return new Sliced<>(mappedContent, hasNext);
    }

    public List<T> getContent() {
        return content;
    }

    public boolean hasNext() {
        return hasNext;
    }
}
