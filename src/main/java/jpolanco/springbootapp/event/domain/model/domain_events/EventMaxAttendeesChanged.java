package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventMaxAttendeesChanged extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final long maxAttendees;

    public EventMaxAttendeesChanged(String eventId, String eventName, long maxAttendees) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.maxAttendees = maxAttendees;
    }
}
