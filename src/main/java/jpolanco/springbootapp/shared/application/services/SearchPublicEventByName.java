package jpolanco.springbootapp.shared.application.services;

import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.uc.SearchPublicEventByNameUC;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchPublicEventByName implements SearchPublicEventByNameUC {
    private final EventQueryRepository queryRepository;
    @Override
    public List<Event> search(String name, int size) {
        return queryRepository.searchByName(name, size);
    }
}
