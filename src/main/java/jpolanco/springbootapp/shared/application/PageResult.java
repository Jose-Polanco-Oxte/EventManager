package jpolanco.springbootapp.shared.application;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.EntityCollection;

import java.util.List;

public record PageResult<E>(
        List<E> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) implements EntityCollection {

    public PageResult(List<E> items, int page, int size, boolean hasNext) {
        this(items, page, size, 0L, 0, hasNext);
    }

    @Override
    public boolean hasContent() {
        return items != null && !items.isEmpty();
    }
}