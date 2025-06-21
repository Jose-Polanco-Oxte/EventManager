package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventScheduleChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventScheduleListener {
    private static final Logger log = LoggerFactory.getLogger(EventScheduleListener.class);

    @EventListener
    public void handleEventScheduled(EventScheduleChanged event) {
        log.info("Event scheduled: eventId= {}, name= {}, timeStamp= {}, scheduledTime= {}",
                 event.getEventId(), event.getEventName(), event.getTimeStamp(), event.getNewSchedule());
    }
}
