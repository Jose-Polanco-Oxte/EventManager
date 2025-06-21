package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventCompleted extends EventNotification {
    private final String eventId;
    private final String eventName;

    public EventCompleted(String eventId, String eventName) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
    }
}
