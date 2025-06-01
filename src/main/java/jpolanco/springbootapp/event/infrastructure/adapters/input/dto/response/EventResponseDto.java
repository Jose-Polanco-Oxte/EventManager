package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response;

import jpolanco.springbootapp.shared.application.Dto;

public record EventResponseDto(
        String id,
        String title,
        String description,
        String schedule,
        long duration,
        String creatorId
) implements Dto {
    // No additional methods needed; the record automatically provides getters and a constructor.
}
