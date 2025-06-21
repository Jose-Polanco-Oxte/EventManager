package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventCreated extends EventNotification {
    private final String eventId;
    private final String name;
    private final String description;
    private final long duration;
    private final String location;
    private final String creatorId;

    public EventCreated(String eventId, String name, String description, long duration, String location, String creatorId) {
        super();
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.location = location;
        this.creatorId = creatorId;
    }

}
