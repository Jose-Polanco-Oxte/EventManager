package jpolanco.applicationcore.user.application.events;

import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserTokenRefreshed extends EventNotification {
    private final UUID userId;
    private final String email;
    private final String refreshToken;
    private final String newToken;
}
