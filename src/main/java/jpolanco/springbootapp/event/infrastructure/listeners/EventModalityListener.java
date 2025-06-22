package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventModalityChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventModalityListener {
    private static final Logger log = LoggerFactory.getLogger(EventModalityListener.class);

    @EventListener
    public void handleEventModalityChanged(EventModalityChanged event) {
        log.info("Event modality changed: Event ID = {}, nam={}, New Modality = {}",
                event.getEventId(), event.getEventName(), event.getModality());
        // Additional logic can be added here, such as updating the event in the database or notifying other services.
    }
}