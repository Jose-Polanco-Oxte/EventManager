package jpolanco.springbootapp.shared.application.utils;

import java.util.List;

public record CursorPageResult<E, ID>(
        List<E> items,
        ID lastItemId,
        boolean hasNextPage
) {
}
