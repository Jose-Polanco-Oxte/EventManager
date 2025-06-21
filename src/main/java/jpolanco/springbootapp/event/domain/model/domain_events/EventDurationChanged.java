package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventDurationChanged extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final long newDuration;

    public EventDurationChanged(String eventId, String eventName, long newDuration) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.newDuration = newDuration;
    }
}
