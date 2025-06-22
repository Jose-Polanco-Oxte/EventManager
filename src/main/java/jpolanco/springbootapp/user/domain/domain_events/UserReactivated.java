package jpolanco.springbootapp.user.domain.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class UserReactivated extends EventNotification {
    private final String userId;

    public UserReactivated(String userId) {
        this.userId = userId;
    }
}
