package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventDurationChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventDurationListener {
    private static final Logger log = LoggerFactory.getLogger(EventDurationListener.class);

    @EventListener
    public void handleEventDuration(EventDurationChanged event) {
        log.info("Event duration: eventId= {}, name= {}, duration= {} ms, timeStamp= {}",
                 event.getEventId(), event.getEventName(), event.getNewDuration(), event.getTimeStamp());
    }
}
