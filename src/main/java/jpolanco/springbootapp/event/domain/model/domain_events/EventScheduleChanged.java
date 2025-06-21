package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

import java.time.Instant;

@Getter
public class EventScheduleChanged extends EventNotification {
    private final String eventId;
    private final Instant newSchedule;
    private final String eventName;

    public EventScheduleChanged(String eventId, Instant newSchedule, String eventName) {
        super();
        this.eventId = eventId;
        this.newSchedule = newSchedule;
        this.eventName = eventName;
    }

}
