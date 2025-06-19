package jpolanco.springbootapp.event.application.services.unique;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.unique.GetAllEventsUC;
import jpolanco.springbootapp.event.domain.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetAllEvents implements GetAllEventsUC {
    private final EventRepository eventRepository;

    @Override
    public List<Event> get() {
        return eventRepository.findAll();
    }
}
