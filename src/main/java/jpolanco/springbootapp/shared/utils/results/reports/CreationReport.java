package jpolanco.springbootapp.shared.utils.results.reports;

import jpolanco.springbootapp.shared.domain.EventNotification;
import jpolanco.springbootapp.shared.utils.results.Report;
import jpolanco.springbootapp.shared.domain.utils.Error;

import java.util.List;

public class CreationReport extends Report {
    private final List<EventNotification> notifications;

    private CreationReport(List<EventNotification> notifications, List<Error> errors) {
        super(errors);
        this.notifications = notifications != null ? notifications : List.of();
    }

    public static  CreationReport created(List<EventNotification> notifications) {
        return new CreationReport(notifications, List.of());
    }

    public static CreationReport failed(List<Error> errors) {
        return new CreationReport(List.of(), errors != null ? errors : List.of());
    }

    public static CreationReport failed(Error error) {
        return new CreationReport(List.of(), error != null ? List.of(error) : List.of());
    }

    public List<EventNotification> getNotifications() {
        return notifications;
    }
}
