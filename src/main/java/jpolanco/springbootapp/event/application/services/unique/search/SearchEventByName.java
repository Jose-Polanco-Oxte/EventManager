package jpolanco.springbootapp.event.application.services.unique.search;

import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.unique.search.SearchEventByNameUC;
import jpolanco.springbootapp.event.domain.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchEventByName implements SearchEventByNameUC {
    private final EventQueryRepository queryRepository;

    @Override
    public List<Event> search(String name, int size) {
        return queryRepository.searchByName(name, size);
    }
}