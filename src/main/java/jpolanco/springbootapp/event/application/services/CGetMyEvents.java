package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.CGetMyEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import jpolanco.springbootapp.shared.application.CursorPageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CGetMyEvents implements CGetMyEventsUC {
    private final EventRepository eventRepository;
    @Override
    public CursorPageResult<Event, String> getMyEvents(String cursor, String creatorId, int size, String sortBy, String sortOrder) {
        return eventRepository.findByCreatorId(creatorId, cursor, size, sortBy, sortOrder);
    }
}
