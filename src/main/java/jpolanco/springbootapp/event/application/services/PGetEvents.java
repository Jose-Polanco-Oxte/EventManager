package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.PGetEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PGetEvents implements PGetEventsUC {
    private final EventRepository eventRepository;
    @Override
    public PageResult<Event> getEvents(int page, int size, String sortBy, String sortOrder) {
        return eventRepository.findAll(page, size, sortBy, sortOrder);
    }
}
