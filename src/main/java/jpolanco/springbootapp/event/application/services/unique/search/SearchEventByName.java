package jpolanco.springbootapp.event.application.services.unique.search;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.unique.search.SearchEventByNameUC;
import jpolanco.springbootapp.event.domain.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchEventByName implements SearchEventByNameUC {
    private final EventRepository eventRepository;

    @Override
    public List<Event> search(String name, int size) {
        return eventRepository.searchByName(name, size);
    }
}