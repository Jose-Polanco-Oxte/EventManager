package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.DataChangeLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.user.domain.events.UserPasswordChanged;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPasswordChangedListener {

    private static final Logger logger = LoggerFactory.getLogger(UserPasswordChangedListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserPasswordChanged(UserPasswordChanged event) {
        logger.info("User password changed: userId= {}, changeDate= {}", event.getUserId(), event.getTimeStamp());

        // Create a DataChangeLog instance
        DataChangeLog dataChangeLog = new DataChangeLog();
        dataChangeLog.setUserId(event.getUserId().toString());
        dataChangeLog.setEntityId(event.getUserId().toString());
        dataChangeLog.setEntity("User");
        dataChangeLog.setAction("PASSWORD_CHANGE");
        
        auditService.saveWebLog(dataChangeLog);
    }
}