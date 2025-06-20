package jpolanco.springbootapp.event.application.services.unique.search;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.unique.search.SearchOwnEventByNameUC;
import jpolanco.springbootapp.event.domain.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchOwnEventByName implements SearchOwnEventByNameUC {
    private final EventRepository eventRepository;

    @Override
    public List<Event> search(String name, String creatorId, int size) {
        return eventRepository.searchMyEventsByName(name, creatorId, size);
    }
}
