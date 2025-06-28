package jpolanco.springbootapp.shared.infrastructure.dto.response;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.EntityCollection;

import java.util.List;

public record SlicePageResponse<T>(
        List<T> content,
        boolean hasNextPage
) implements EntityCollection, Dto {

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }
}
