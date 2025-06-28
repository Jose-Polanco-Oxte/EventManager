package jpolanco.springbootapp.user.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "Old password should not be blank")
        String oldPassword,
        @NotBlank(message = "New password should not be blank")
        String newPassword
) {
}
