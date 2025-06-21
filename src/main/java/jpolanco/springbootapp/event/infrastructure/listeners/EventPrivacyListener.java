package jpolanco.springbootapp.event.infrastructure.listeners;

import jpolanco.springbootapp.event.domain.model.domain_events.EventPrivacyChanged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventPrivacyListener {
    private static final Logger log = LoggerFactory.getLogger(EventPrivacyListener.class);

    @EventListener
    public void handleEventPrivacyChanged(EventPrivacyChanged event) {
        log.info("Event privacy changed: eventId= {}, name= {}, newPrivacy= {}, timeStamp= {}",
                 event.getEventId(), event.getEventName(), event.isPublic() ? "public" : "private", event.getTimeStamp());
    }
}
