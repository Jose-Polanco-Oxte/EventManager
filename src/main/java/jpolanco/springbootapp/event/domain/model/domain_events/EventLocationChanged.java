package jpolanco.springbootapp.event.domain.model.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class EventLocationChanged extends EventNotification {
    private final String eventId;
    private final String eventName;
    private final double latitude;
    private final double longitude;
    private final String locationName;
    private final String locationCity;
    private final String locationCountry;

    public EventLocationChanged(String eventId, String eventName, double latitude, double longitude,
                                String locationName, String locationCity, String locationCountry) {
        super();
        this.eventId = eventId;
        this.eventName = eventName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
        this.locationCity = locationCity;
        this.locationCountry = locationCountry;
    }
}
