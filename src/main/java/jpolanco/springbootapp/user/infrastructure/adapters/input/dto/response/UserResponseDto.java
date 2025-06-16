package jpolanco.springbootapp.user.infrastructure.adapters.input.dto.response;

import jpolanco.springbootapp.shared.application.Dto;

import java.util.List;

public record UserResponseDto(
        String id,
        String firstName,
        String lastName,
        String email,
        String status,
        List<String> roles
) implements Dto {
}
