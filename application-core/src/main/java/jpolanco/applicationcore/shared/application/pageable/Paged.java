package jpolanco.applicationcore.shared.application.pageable;

import lombok.Getter;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.function.Function;

/**
 * Paged class for pagination with total elements and total pages information.
 *
 * @param <T> the type of content in the page.
 *            <p>
 *            {@link #getTotalElements()} returns the total number of elements across all pages.
 *            {@link #getTotalPages()} returns the total number of pages available.
 *            {@link #getSize()} returns the size of the page (number of items per page).
 *            </p>
 */
@Getter
public class Paged<T> extends Sliced<T> {

    private final long totalElements;

    private final int totalPages;

    private final int size;

    public Paged(List<T> content, boolean hasNext, long totalElements, int totalPages, int size) {
        super(content, hasNext);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
    }

    // Method to map the content of the Paged to another type.
    public <R> Paged<R> map(Function<T, R> mapper) {
        List<R> mappedContent = Streamable.of(getContent()).map(mapper).toList();
        return new Paged<>(mappedContent, hasNext(), totalElements, totalPages, size);
    }
}
