package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.PGetPublicEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.utils.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PGetPublicEvents implements PGetPublicEventsUC {
    private final EventRepository eventRepository;
    @Override
    public PageResult<Event> getPublicEvents(int page, int size, String sortBy, String sortOrder) {
        return eventRepository.findPublicEvents(page, size, sortBy, sortOrder);
    }
}
