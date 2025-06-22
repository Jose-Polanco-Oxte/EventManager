package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventRestored;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventRestoredListener {
    private static final Logger log = LoggerFactory.getLogger(EventRestoredListener.class);

    @EventListener
    public void handleEventRestored(EventRestored event) {
        log.info("Event restored: eventId= {}, name= {}, timeStamp= {}, message= {}",
                 event.getEventId(), event.getEventName(), event.getTimeStamp(), event.getMessageToAttendees());
    }
}