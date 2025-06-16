package jpolanco.springbootapp.shared.application.services;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.uc.SearchPublicEventByNameUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchPublicEventByName implements SearchPublicEventByNameUC {
    private final EventRepository eventRepository;
    @Override
    public List<Event> search(String name, int size) {
        return eventRepository.searchByName(name, size);
    }
}
