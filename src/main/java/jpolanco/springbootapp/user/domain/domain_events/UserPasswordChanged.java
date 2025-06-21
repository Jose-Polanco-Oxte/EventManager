package jpolanco.springbootapp.user.domain.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;

public class UserPasswordChanged extends EventNotification {
    private final String userId;

    public UserPasswordChanged(String userId) {
        super();
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
