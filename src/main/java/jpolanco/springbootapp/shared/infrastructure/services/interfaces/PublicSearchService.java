package jpolanco.springbootapp.shared.infrastructure.services.interfaces;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;

import java.util.List;

public interface PublicSearchService {
    List<EventResponseDto> searchPublicEventsByName(String name, int size);

    List<EventResponseDto> searchPublicEventsByCategory(String category, int size);
}
