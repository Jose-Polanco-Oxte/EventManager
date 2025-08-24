package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.AuthLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.user.domain.events.UserRegistered;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredListener {

    private static final Logger logger = LoggerFactory.getLogger(UserRegisteredListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserRegistered(UserRegistered event) {
        logger.info("User registered: userId= {}, email= {}, registerDate= {}", event.getUserId(), event.getEmail(), event.getTimeStamp());

        // Create a DataChangeLog instance
        AuthLog authLog = new AuthLog();
        authLog.setUserId(event.getUserId().toString());
        authLog.setEmail(event.getEmail());
        authLog.setAction("REGISTER");
        
        auditService.saveWebLog(authLog);
    }
}
