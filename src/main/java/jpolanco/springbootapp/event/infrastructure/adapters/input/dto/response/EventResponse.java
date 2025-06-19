package jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response;

import jpolanco.springbootapp.shared.infrastructure.dto.Dto;

public record EventResponse(
        String id,
        String title,
        String description,
        String schedule,
        long duration,
        String creatorId
) implements Dto {
    // No additional methods needed; the record automatically provides getters and a constructor.
}
