package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventMaxAttendeesChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventMaxAttendeesListener {
    private static final Logger log = LoggerFactory.getLogger(EventMaxAttendeesListener.class);

    @EventListener
    public void handleEventMaxAttendees(EventMaxAttendeesChanged event) {
        log.info("Event max attendees reached: eventId= {}, name= {}, maxAttendees= {}, timeStamp= {}",
                 event.getEventId(), event.getEventName(), event.getMaxAttendees(), event.getTimeStamp());
    }
}
