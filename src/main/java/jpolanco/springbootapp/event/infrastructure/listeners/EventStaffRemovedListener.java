package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventStaffRemoved;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventStaffRemovedListener {
    private static final Logger log = LoggerFactory.getLogger(EventStaffRemovedListener.class);

    @EventListener
    public void handleEventStaffRemoved(EventStaffRemoved event) {
        log.info("Staff removed from event: eventId= {}, name= {}, timeStamp= {}",
                 event.getEventId(), event.getEventName(), event.getTimeStamp());
        // Log each staff request
        event.getStaffRequests().forEach((staffRequest) -> {
            log.info("Staff member removed: staffId= {}, role= {}",
                     staffRequest.staffId(), staffRequest.role());
        });
    }
}
