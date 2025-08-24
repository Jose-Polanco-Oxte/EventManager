package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.AuthLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.user.application.events.UserTokenRefreshed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserTokenRefreshedListener {

    private static final Logger log = LoggerFactory.getLogger(UserTokenRefreshedListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserTokenRefreshed(UserTokenRefreshed event) {
        log.info("User token refreshed: userId= {}, token= {}", event.getUserId(), event.getRefreshToken());

        // Create a DataChangeLog instance
        AuthLog authLog = new AuthLog();
        authLog.setUserId(event.getUserId().toString());
        authLog.setEmail(event.getUserId().toString());
        authLog.setAction("TOKEN_REFRESH");
        authLog.setDetails(Map.of(
                "token", event.getRefreshToken(),
                "newRefreshToken", event.getNewToken()));
        
        auditService.saveWebLog(authLog);
    }
}