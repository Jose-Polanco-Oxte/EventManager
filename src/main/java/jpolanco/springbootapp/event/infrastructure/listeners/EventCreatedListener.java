package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventCreated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventCreatedListener {
    private static final Logger log = LoggerFactory.getLogger(EventCreatedListener.class);

    @EventListener
    public void handleEventCreated(EventCreated event) {
        log.info("Event created: eventId= {}, name= {}, timeStamp= {}, creatorId= {}",
                 event.getEventId(), event.getName(), event.getTimeStamp(), event.getCreatorId());
    }
}
