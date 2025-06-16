package jpolanco.springbootapp.shared.infrastructure.dto;

import jpolanco.springbootapp.shared.application.Dto;

import java.util.List;

public record SlicePageResponseDto<T>(
        List<T> content,
        boolean hasNext
) implements Dto {
}
