package jpolanco.springbootapp.event.application.services;

import jpolanco.springbootapp.event.application.ports.input.FileStorageProvider;
import jpolanco.springbootapp.event.application.ports.output.EventRepository;
import jpolanco.springbootapp.event.application.uc.UpdateEventUC;
import jpolanco.springbootapp.event.application.utils.EventUpdater;
import jpolanco.springbootapp.event.application.utils.EventValidation;
import jpolanco.springbootapp.event.infrastructure.errors.EventIntegrity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("Any")
public class UpdateEvent implements UpdateEventUC {
    private final EventRepository eventRepository;
    private final EventValidation eventValidation;
    private final FileStorageProvider fileStorageProvider;
    @Override
    public EventUpdater setChanges(String eventId) {
        var maybeEvent = eventRepository.findById(eventId);
        if (maybeEvent.isEmpty()) {
            throw new EventIntegrity("Event with ID " + eventId + " does not exist.");
        }
        return new EventUpdater(maybeEvent.get(), fileStorageProvider, eventValidation);
    }
}
