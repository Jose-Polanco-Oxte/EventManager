package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequest(
        @NotBlank(message = "Email should not be blank") @Email(message = "Email should be valid") String email,
        @Email(message = "Confirm email should be valid") @NotBlank(message = "Confirm email should not be blank") String confirmEmail) {
}
