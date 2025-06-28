package jpolanco.springbootapp.shared.infrastructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommandReasonRequest(
        @NotBlank(message = "Reason cannot be blank")
        @NotNull(message = "Reason cannot be null")
        String reason
) {
}
