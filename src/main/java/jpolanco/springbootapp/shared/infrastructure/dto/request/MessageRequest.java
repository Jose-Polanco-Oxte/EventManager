package jpolanco.springbootapp.shared.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
        @NotBlank(message = "Message cannot be blank")
        @NotNull(message = "Message cannot be null")
        String message
) {
}
