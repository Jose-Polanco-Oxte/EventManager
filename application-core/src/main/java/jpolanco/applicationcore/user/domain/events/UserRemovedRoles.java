package jpolanco.applicationcore.user.domain.events;

import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserRemovedRoles extends EventNotification {
    private final UUID userId;
    private final String email;
    private final List<String> before;
    private final List<String> after;
    private final List<String> roles;
}
