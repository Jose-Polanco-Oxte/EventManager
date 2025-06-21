package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.application.utils.Changes;
import jpolanco.springbootapp.event.domain.model.domain_events.EventUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventUpdateListener {
    private static final Logger log = LoggerFactory.getLogger(EventUpdateListener.class);

    @EventListener
    public void handleEventUpdate(EventUpdate event) {
        var eventId = event.getEventId();
        var name = event.getEventName();
        List<Changes<?>> changes = event.getChanges();
        // Log the event details and after changes
        log.info("Event updated: eventId= {}, name= {}, timeStamp= {}",
                 eventId, name, event.getTimeStamp());
        // Log each change
        changes.forEach(change ->
            log.info("Change detected: field= {}, oldValue= {}, newValue= {}",
                     change.fieldName(), change.oldValue(), change.newValue()));
    }
}
