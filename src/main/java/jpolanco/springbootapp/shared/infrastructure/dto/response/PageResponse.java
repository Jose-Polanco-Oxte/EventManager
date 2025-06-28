package jpolanco.springbootapp.shared.infrastructure.dto.response;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.EntityCollection;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        boolean hasNextPage
) implements EntityCollection, Dto {

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }
}
