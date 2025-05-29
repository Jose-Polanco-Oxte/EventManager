package jpolanco.springbootapp.user.application.events;

import jpolanco.springbootapp.shared.domain.DomainEvent;
import lombok.Getter;
@Getter
public class UserRegistered extends DomainEvent {
    private final String userId;
    private final String email;

    public UserRegistered(String userId, String email) {
        super();
        this.userId = userId;
        this.email = email;
    }
}
