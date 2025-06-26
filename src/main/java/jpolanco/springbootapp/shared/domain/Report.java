package jpolanco.springbootapp.shared.domain;

import jpolanco.springbootapp.event.application.utils.Changes;
import lombok.Getter;

import java.util.List;


public class Report {
    @Getter
    private final List<Error> errors;
    @Getter
    private final List<Changes<?>> changes;
    @Getter
    private final List<EventNotification> notifications;
    @Getter
    private final boolean success;
    private final boolean hasChanges;
    private final boolean hasNotifications;

    private Report(List<Error> errors, List<Changes<?>> changes, boolean success, List<EventNotification> notifications, boolean hasNotifications) {
        this.errors = errors;
        this.changes = changes;
        this.success = success;
        this.hasNotifications = hasNotifications;
        this.notifications = notifications;
        this.hasChanges = !changes.isEmpty();
    }

    public static Report success(List<Changes<?>> changes, List<EventNotification> notifications) {
        return new Report(List.of(), changes, true, notifications, !notifications.isEmpty());
    }

    public static Report failure(List<Error> errors) {
        return new Report(errors, List.of(), false, List.of(), false);
    }

    public static Report failure(Error error) {
        return new Report(List.of(error), List.of(), false, List.of(), false);
    }

    public Report toVoid() {
        return new Report(this.errors, this.changes, this.success, this.notifications, this.hasNotifications);
    }

    public void addError(Error error) {
        if (error != null) {
            this.errors.add(error);
        }
    }

    public void addDomainEvent(EventNotification event) {
        if (event != null) {
            this.notifications.add(event);
        }
    }

    public boolean isFailure() {
        return !success;
    }

    public boolean hasChanges() {
        return hasChanges;
    }

    public boolean hasNotifications() {
        return hasNotifications;
    }
}