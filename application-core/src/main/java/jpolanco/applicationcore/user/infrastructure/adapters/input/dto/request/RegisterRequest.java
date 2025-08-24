package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank(message = "First word is required") String firstName,
                              @NotBlank(message = "Last word is required") String lastName,
                              @NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,
                              @NotBlank(message = "Password is required") @Size(min = 5, max = 72, message = "Password must be between 5 and 72 characters") String password) {
}
