package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventPrivacyChanged extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final boolean isPublic;

    public EventPrivacyChanged(String eventId, String eventName, boolean isPublic) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.isPublic = isPublic;
    }
}
