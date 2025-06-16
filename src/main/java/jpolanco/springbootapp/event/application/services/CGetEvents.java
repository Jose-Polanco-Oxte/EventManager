package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.CGetEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CGetEvents implements CGetEventsUC {
    private final EventRepository eventRepository;
    @Override
    public CursorPageResult<Event, String> getEvents(String cursor, int size, String sortBy, String sortOrder) {
        return eventRepository.findAll(cursor, size, sortBy, sortOrder);
    }
}
