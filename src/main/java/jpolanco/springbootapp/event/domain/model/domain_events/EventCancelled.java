package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventCancelled extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final String reason;

    public EventCancelled(String eventId, String eventName, String reason) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.reason = reason;
    }
}
