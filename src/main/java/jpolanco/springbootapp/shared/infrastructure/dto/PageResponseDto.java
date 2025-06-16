package jpolanco.springbootapp.shared.infrastructure.dto;

import jpolanco.springbootapp.shared.application.Dto;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        boolean hasNext
) implements Dto {
}
