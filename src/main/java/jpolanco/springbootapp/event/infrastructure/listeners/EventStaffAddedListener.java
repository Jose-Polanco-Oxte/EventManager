package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventStaffAdded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventStaffAddedListener {
    private static final Logger log = LoggerFactory.getLogger(EventStaffAddedListener.class);

    @EventListener
    public void handleEventStaffAdded(EventStaffAdded event) {
        log.info("Staff added to event: eventId= {}, name= {}, timeStamp= {}",
                 event.getEventId(), event.getEventName(), event.getTimeStamp());
        // each staff member can be logged individually if needed
        event.getStaffRequests().forEach(staffRequest ->
            log.info("Staff member added: staffId= {}, role= {}",
                     staffRequest.staffId(), staffRequest.role()));
    }
}
