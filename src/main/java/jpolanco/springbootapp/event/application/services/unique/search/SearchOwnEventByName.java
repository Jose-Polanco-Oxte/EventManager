package jpolanco.springbootapp.event.application.services.unique.search;

import jpolanco.springbootapp.event.application.ports.output.EventQueryRepository;
import jpolanco.springbootapp.event.application.uc.unique.search.SearchOwnEventByNameUC;
import jpolanco.springbootapp.event.domain.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchOwnEventByName implements SearchOwnEventByNameUC {
    private final EventQueryRepository queryRepository;

    @Override
    public List<Event> search(String name, String creatorId, int size) {
        return queryRepository.searchMyEventsByName(name, creatorId, size);
    }
}
