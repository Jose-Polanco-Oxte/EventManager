package jpolanco.springbootapp.shared.infrastructure.dto;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        boolean hasNext
) implements Dto {
}
