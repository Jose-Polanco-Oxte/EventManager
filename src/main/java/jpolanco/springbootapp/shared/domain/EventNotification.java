package jpolanco.springbootapp.shared.domain;

import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class EventNotification {
    private Instant timeStamp;

    public EventNotification() {
        this.timeStamp = Instant.now();
    }

}
