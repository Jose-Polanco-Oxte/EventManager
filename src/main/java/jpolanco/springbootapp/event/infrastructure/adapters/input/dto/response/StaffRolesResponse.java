package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;

public record StaffRolesResponse(
        String name
) implements Dto {
}
