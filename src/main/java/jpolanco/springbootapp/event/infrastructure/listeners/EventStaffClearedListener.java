package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventStaffCleared;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventStaffClearedListener {
    private static final Logger log = LoggerFactory.getLogger(EventStaffClearedListener.class);

    @EventListener
    public void handleEventStaffCleared(EventStaffCleared event) {
        log.info("Event staff cleared: eventId= {}, name={} timeStamp= {}",
                 event.getEventId(), event.getEventName(), event.getTimeStamp());
        // Log each staff request that was cleared
        event.getStaffRequests().forEach((staffRequest) -> {
            log.info("Staff member cleared: staffId= {}, role= {}",
                     staffRequest.staffId(), staffRequest.role());
        });
    }
}
