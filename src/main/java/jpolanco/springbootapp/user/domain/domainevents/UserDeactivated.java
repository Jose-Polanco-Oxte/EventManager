package jpolanco.springbootapp.user.domain.domainevents;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserDeactivated extends EventNotification {
    private final UUID userId;
    private final String reason;
}
