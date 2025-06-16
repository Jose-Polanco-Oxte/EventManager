package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response;

import jpolanco.springbootapp.shared.application.Dto;

public record StaffRolesResponseDto(
        String name
) implements Dto {
}
