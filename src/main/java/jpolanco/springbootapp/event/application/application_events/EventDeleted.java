package jpolanco.springbootapp.event.application.application_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventDeleted extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final String reason;

    public EventDeleted(String eventId, String eventName, String reason) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.reason = reason;
    }
}
