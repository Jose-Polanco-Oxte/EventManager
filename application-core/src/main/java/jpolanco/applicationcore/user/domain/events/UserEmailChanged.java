package jpolanco.applicationcore.user.domain.events;

import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@Getter
@AllArgsConstructor
public class UserEmailChanged extends EventNotification {
    private final UUID userId;
    private final String before;
    private final String after;
}