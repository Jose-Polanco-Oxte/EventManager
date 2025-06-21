package jpolanco.springbootapp.user.domain.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;

public class UserSuspended extends EventNotification {
    private final String userId;
    private final String reason;

    public UserSuspended(String userId, String reason) {
        super();
        this.userId = userId;
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }
}
