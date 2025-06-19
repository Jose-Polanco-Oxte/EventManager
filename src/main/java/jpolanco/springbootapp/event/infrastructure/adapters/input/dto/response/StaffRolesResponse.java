package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response;

import jpolanco.springbootapp.shared.infrastructure.dto.Dto;

public record StaffRolesResponse(
        String name
) implements Dto {
}
