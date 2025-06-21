package jpolanco.springbootapp.user.domain.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class UserRegistered extends EventNotification {
    private final String userId;
    private final String email;

    public UserRegistered(String userId, String email) {
        super();
        this.userId = userId;
        this.email = email;
    }
}
