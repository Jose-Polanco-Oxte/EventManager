package jpolanco.springbootapp.shared.infrastructure;

import jpolanco.springbootapp.shared.infrastructure.dto.Dto;

public record SimpleResponseDto(String message) implements Dto {
}
