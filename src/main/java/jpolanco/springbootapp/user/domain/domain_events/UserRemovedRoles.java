package jpolanco.springbootapp.user.domain.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.Getter;

import java.util.List;

@Getter
public class UserRemovedRoles extends EventNotification {
    private final String userId;
    private final String email;
    private final List<String> roles;

    public UserRemovedRoles(String userId, String email, List<String> roles) {
        super();
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }
}
