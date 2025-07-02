package jpolanco.springbootapp.user.domain.domain_events;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserEmailChanged extends EventNotification {
    private final UUID userId;
    private final String oldEmail;
    private final String newEmail;
}