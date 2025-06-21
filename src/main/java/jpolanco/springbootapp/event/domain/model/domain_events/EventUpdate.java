package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.event.application.utils.Changes;
import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

import java.util.List;

@Getter
public class EventUpdate extends EventNotification {
    private final String eventId;
    private final String eventName;
    List<Changes<?>> changes;

    public EventUpdate(String eventId, String eventName, List<Changes<?>> changes) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.changes = changes;
    }
}
