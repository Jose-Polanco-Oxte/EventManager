package jpolanco.springbootapp.user.domain.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

@Getter
public class UserEmailChanged extends EventNotification {
    private final String userId;
    private final String oldEmail;
    private final String newEmail;

    public UserEmailChanged(String userId, String oldEmail, String newEmail) {
        super();
        this.userId = userId;
        this.oldEmail = oldEmail;
        this.newEmail = newEmail;
    }
}
