package jpolanco.springbootapp.user.domain.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class UserDeactivated extends EventNotification {
    private final String userId;
    private final String reason;

    public UserDeactivated(String userId, String reason) {
        this.userId = userId;
        this.reason = reason;
    }
}
