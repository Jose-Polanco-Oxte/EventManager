package jpolanco.applicationcore.shared.domain.notify;

import java.time.Instant;

/**
 * Base class for event notifications.
 */
public abstract class EventNotification {
    private final Instant timeStamp;

    public EventNotification() {
        this.timeStamp = Instant.now();
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }
}
