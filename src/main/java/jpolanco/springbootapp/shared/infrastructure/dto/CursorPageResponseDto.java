package jpolanco.springbootapp.shared.infrastructure.dto;

import java.util.List;

public record CursorPageResponseDto<T, ID>(
        List<T> items,
        ID lastItemId,
        boolean hasNextPage
) implements Dto {
}
