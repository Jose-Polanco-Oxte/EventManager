package jpolanco.applicationcore.user.infrastructure.adapters.input.dto.request;

import jakarta.validation.constraints.Min;

import java.util.Optional;

public record SearchQueryRequest(String query, @Min(1) Optional<Integer> size) {
}
