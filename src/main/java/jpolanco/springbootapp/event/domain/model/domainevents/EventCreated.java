package jpolanco.springbootapp.event.domain.model.domainevents;

import jpolanco.springbootapp.shared.domain.DomainEvent;

public class EventCreated extends DomainEvent {
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

    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDuration() {
        return duration;
    }

    public String getLocation() {
        return location;
    }

    public String getCreatorId() {
        return creatorId;
    }
}
