package jpolanco.applicationcore.shared.application.pageable;

import lombok.Getter;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.function.Function;

/**
 * Cursored class for pagination with cursor-based navigation.
 *
 * @param <T> the type of content in the page.
 *            It extends Sliced to include cursor functionality.
 */

@Getter
public class Cursored<T> extends Sliced<T> {

    private final Object nextCursor; // The cursor for the next page.

    public Cursored(List<T> content, boolean hasNext, Object nextCursor) {
        super(content, hasNext);
        this.nextCursor = nextCursor;
    }

    // Static method to create an empty Cursored instance.
    public static <T> Cursored<T> empty() {
        return new Cursored<>(List.of(), false, null);
    }

    // Method to map the content of the Cursored to another type.
    public <R> Cursored<R> map(Function<T, R> mapper) {
        List<R> mappedContent = Streamable.of(getContent()).map(mapper).toList();
        return new Cursored<>(mappedContent, hasNext(), nextCursor);
    }
}
