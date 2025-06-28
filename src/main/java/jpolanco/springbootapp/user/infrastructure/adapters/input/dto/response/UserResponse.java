package jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response;

import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.Dto;

import java.util.List;

public record UserResponse(
        String id,
        String firstName,
        String lastName,
        String email,
        String status,
        List<String> roles
) implements Dto {
}
