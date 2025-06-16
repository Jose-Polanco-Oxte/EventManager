package jpolanco.springbootapp.event.domain.model.domainevents;

import jpolanco.springbootapp.shared.domain.DomainEvent;

public class EventCanceled extends DomainEvent {
    private final String eventId;
    private final String reason;

    public EventCanceled(String eventId, String reason) {
        super();
        this.eventId = eventId;
        this.reason = reason;
    }

    public String getEventId() {
        return eventId;
    }

    public String getReason() {
        return reason;
    }
}
