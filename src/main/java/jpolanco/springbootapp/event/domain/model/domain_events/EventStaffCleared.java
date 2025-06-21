package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.event.application.ports.input.request.StaffRequest;
import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

import java.util.List;

@Getter
public class EventStaffCleared extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final List<StaffRequest> staffRequests;

    public EventStaffCleared(String eventId, String eventName, List<StaffRequest> staffRequests) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.staffRequests = staffRequests;
    }
}
