package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventCancelled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventCancelledListener {
    private static final Logger log = LoggerFactory.getLogger(EventCancelledListener.class);

    @EventListener
    public void handleEventCancelled(EventCancelled event) {
        log.info("Event cancelled: eventId= {}, eventName={} reason={} timeStamp= {}",
                 event.getEventId(), event.getEventName(), event.getReason(), event.getTimeStamp());
    }
}
