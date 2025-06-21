package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventCompleted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventCompletedListener {
    private static final Logger log = LoggerFactory.getLogger(EventCompletedListener.class);

    @EventListener
    public void handleEventCompleted(EventCompleted event) {
        log.info("Event completed: eventId= {}, name= {}, timeStamp= {}",
                 event.getEventId(), event.getEventName(), event.getTimeStamp());
    }
}
