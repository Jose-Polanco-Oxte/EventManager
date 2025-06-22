package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventRestored extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final String messageToAttendees;

    public EventRestored(String eventId, String eventName, String messageToAttendees) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.messageToAttendees = messageToAttendees;
    }
}
