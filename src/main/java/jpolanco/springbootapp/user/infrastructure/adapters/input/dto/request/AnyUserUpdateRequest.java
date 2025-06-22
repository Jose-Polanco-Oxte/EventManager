package jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jpolanco.springbootapp.shared.infrastructure.dto.Dto;


public record AnyUserUpdateRequest(
        @NotBlank(message = "First name cannot be empty")
        String firstName,
        @NotBlank(message = "Last name cannot be empty")
        String lastName,
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Password cannot be empty")
        String password,
        String status,
        UserRoleChangeRequest roles
) implements Dto {
}
