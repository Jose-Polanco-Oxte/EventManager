package jpolanco.applicationcore.user.domain.events;

import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import jpolanco.applicationcore.user.domain.model.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserSuspended extends EventNotification {
    private final UUID userId;
    private final String reason;
    private final UserStatus before;
    private final UserStatus after;
}
