package jpolanco.springbootapp.shared.domain;

import jpolanco.springbootapp.event.application.utils.Changes;
import jpolanco.springbootapp.shared.domain.utils.Error;
import jpolanco.springbootapp.shared.infrastructure.dto.interfaces.EntityCollection;
import lombok.Getter;

import java.util.List;


public class UpdateReport extends Report implements EntityCollection {
    @Getter
    private final List<Changes> changes;
    @Getter
    private final List<EventNotification> notifications;
    @Getter
    private final boolean success;
    private final boolean hasChanges;

    private UpdateReport(List<Error> errors, List<Changes> changes, boolean success, List<EventNotification> notifications) {
        super(errors);
        this.changes = changes;
        this.success = success;
        this.notifications = notifications;
        this.hasChanges = !changes.isEmpty();
    }

    public static UpdateReport success(List<Changes> changes, List<EventNotification> notifications) {
        return new UpdateReport(List.of(), changes, true, notifications);
    }

    public static UpdateReport failure(List<Error> errors) {
        return new UpdateReport(errors, List.of(), false, List.of());
    }

    public static UpdateReport failure(Error error) {
        return new UpdateReport(List.of(error), List.of(), false, List.of());
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

    @Override
    public boolean hasContent() {
        return !changes.isEmpty();
    }

    @Override
    public String toString() {
        return "UpdateReport{" +
                "errors=" + getErrors() +
                ", changes=" + changes +
                ", notifications=" + notifications +
                ", success=" + success +
                ", hasChanges=" + hasChanges +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateReport)) return false;
        UpdateReport that = (UpdateReport) o;
        return success == that.success &&
                hasChanges == that.hasChanges &&
                changes.equals(that.changes) &&
                notifications.equals(that.notifications) &&
                getErrors().equals(that.getErrors());
    }

    @Override
    public int hashCode() {
        int result = getErrors().hashCode();
        result = 31 * result + changes.hashCode();
        result = 31 * result + notifications.hashCode();
        result = 31 * result + (success ? 1 : 0);
        result = 31 * result + (hasChanges ? 1 : 0);
        return result;
    }
}