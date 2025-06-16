package jpolanco.springbootapp.shared.infrastructure.services;

import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponseDto;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.shared.application.uc.SearchPublicEventByNameUC;
import jpolanco.springbootapp.shared.infrastructure.services.interfaces.PublicSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PublicSearchServiceImpl implements PublicSearchService {
    private final SearchPublicEventByNameUC searchPublicEventByNameUC;
    private final EventDtoCreator eventDtoCreator;
    @Override
    public List<EventResponseDto> searchPublicEventsByName(String name, int size) {
        return searchPublicEventByNameUC.search(name, size)
                .stream()
                .map(eventDtoCreator::create)
                .toList();
    }

    @Override
    public List<EventResponseDto> searchPublicEventsByCategory(String category, int size) {
        return List.of();
    }
}
