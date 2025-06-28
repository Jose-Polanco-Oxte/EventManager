package jpolanco.springbootapp.shared.application;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.EntityCollection;

import java.util.List;

public record CursorPageResult<E, ID>(
        List<E> items,
        ID lastItemId,
        boolean hasNextPage
) implements EntityCollection {

    @Override
    public boolean hasContent() {
        return items != null && !items.isEmpty();
    }
}
