package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventLocationChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventLocationListener {
    private static final Logger log = LoggerFactory.getLogger(EventLocationListener.class);

    @EventListener
    public void handleEventLocationChanged(EventLocationChanged event) {
        log.info("Event location changed: eventId= {}, name= {}, newLocation= {}, timeStamp= {}",
                 event.getEventId(), event.getEventName(),
                event.getLocationName() + " " + event.getLocationCity() + " " + event.getLocationCountry(),
                event.getTimeStamp());
    }
}
