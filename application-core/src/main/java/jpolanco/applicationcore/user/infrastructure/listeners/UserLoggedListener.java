package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.AuthLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.user.application.events.UserLogged;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLoggedListener {

    private static final Logger log = LoggerFactory.getLogger(UserLoggedListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserLogged(UserLogged event) {
        log.info("User logged in: userId= {}, email= {}", event.getUserId(), event.getEmail());

        // Create a DataChangeLog instance
        AuthLog authLog = new AuthLog();
        authLog.setUserId(event.getUserId().toString());
        authLog.setEmail(event.getEmail());
        authLog.setAction("LOGIN");
        
        auditService.saveWebLog(authLog);
    }
}