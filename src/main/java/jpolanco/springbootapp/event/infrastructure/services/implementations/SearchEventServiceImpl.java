package jpolanco.springbootapp.event.infrastructure.services.implementations;

import jpolanco.springbootapp.event.application.uc.unique.search.SearchEventByNameUC;
import jpolanco.springbootapp.event.application.uc.unique.search.SearchOwnEventByNameUC;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.CategoriesResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.EventResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.input.dto.response.StaffRolesResponse;
import jpolanco.springbootapp.event.infrastructure.adapters.mappers.dto.EventDtoCreator;
import jpolanco.springbootapp.event.infrastructure.services.interfaces.SearchEventService;
import jpolanco.springbootapp.shared.application.uc.SearchPublicEventByNameUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchEventServiceImpl implements SearchEventService {

    private final SearchEventByNameUC searchEventByNameUC;
    private final SearchOwnEventByNameUC searchOwnEventByNameUC;
    private final SearchPublicEventByNameUC searchPublicEventByNameUC;
    private final EventDtoCreator eventDtoCreator;

    @Override
    public List<EventResponse> searchEventsByName(String name, int size) {
        var events = searchEventByNameUC.search(name, size);
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(eventDtoCreator::create)
                .toList();
    }

    @Override
    public List<EventResponse> searchMyEventsByName(String name, String creatorId, int size) {
        var events = searchOwnEventByNameUC.search(name, creatorId, size);
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(eventDtoCreator::create)
                .toList();
    }

    @Override
    public List<CategoriesResponse> searchCategoriesByName(String name, int size) {
        return List.of();
    }

    @Override
    public List<StaffRolesResponse> searchStaffRolesByName(String name, int size) {
        return List.of();
    }

    @Override
    public List<EventResponse> searchPublicEventsByName(String name, int size) {
        var events = searchPublicEventByNameUC.search(name, size);
        if (events.isEmpty()) {
            return List.of();
        }
        return events.stream()
                .map(eventDtoCreator::create)
                .toList();
    }

    @Override
    public List<EventResponse> searchPublicEventsByCategory(String category, int size) {
        return List.of();
    }
}
