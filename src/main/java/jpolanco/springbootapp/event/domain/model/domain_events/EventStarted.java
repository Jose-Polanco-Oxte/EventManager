package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

import java.time.Instant;

@Getter
public class EventStarted extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final Instant scheduledStartTime;

    public EventStarted(String eventId, String eventName, Instant scheduledStartTime) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.scheduledStartTime = scheduledStartTime;
    }
}
