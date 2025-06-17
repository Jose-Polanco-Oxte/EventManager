package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.PGetMyEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.utils.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PGetMyEvents implements PGetMyEventsUC {

    private final EventRepository eventRepository;

    @Override
    public PageResult<Event> getMyEvents(String creatorId, int page, int size, String sortBy, String sortOrder) {
        return eventRepository.findByCreatorId(creatorId, page, size, sortBy, sortOrder);
    }
}