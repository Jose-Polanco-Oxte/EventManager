package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.response;

import java.util.List;
import java.util.UUID;

public record UserResponse(UUID id, String firstName, String lastName, String email, String status,
                           List<String> roles) {
}
