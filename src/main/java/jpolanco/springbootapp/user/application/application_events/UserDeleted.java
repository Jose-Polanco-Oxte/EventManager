package jpolanco.springbootapp.user.application.application_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class UserDeleted extends EventNotification {
    private final String userId;
    private final String reason;

    public UserDeleted(String userId, String reason) {
        super();
        this.userId = userId;
        this.reason = reason;
    }
}
