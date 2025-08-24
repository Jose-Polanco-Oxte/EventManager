package jpolanco.applicationcore.shared.application.pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class PageMapper {

    public static <T> Paged<T> from(Page<T> page) {
        return new Paged<>(
                page.getContent(),
                page.hasNext(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSize()
        );
    }

    public static <T> Sliced<T> from(Slice<T> slice) {
        return new Sliced<>(
                slice.getContent(),
                slice.hasNext()
        );
    }

    public static <T> Cursored<T> toCursor(Slice<T> slice) {
        return new Cursored<>(
                slice.getContent(),
                slice.hasNext(),
                slice.getContent().getLast()
        );
    }
}
