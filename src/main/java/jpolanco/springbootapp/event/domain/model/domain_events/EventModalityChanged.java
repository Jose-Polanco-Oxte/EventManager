package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.event.domain.model.valueobjects.Modality;
import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventModalityChanged extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final Modality modality;

    public EventModalityChanged(String eventId, String eventName, Modality modality) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.modality = modality;
    }
}
