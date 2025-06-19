package jpolanco.springbootapp.shared.utils;

import java.util.List;

public record CursorPageResult<E, ID>(
        List<E> items,
        ID lastItemId,
        boolean hasNextPage
) {
}
