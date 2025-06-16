package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.SearchMyEventByNameUC;
import jpolanco.springbootapp.event.domain.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchMyEventByName implements SearchMyEventByNameUC {
    private final EventRepository eventRepository;
    @Override
    public List<Event> searchByName(String name, String creatorId, int size) {
        return eventRepository.searchMyEventsByName(name, creatorId, size);
    }
}
