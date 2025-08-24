package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.DataChangeLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.user.application.events.UserDeleted;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserDeletedListener {

    private static final Logger logger = LoggerFactory.getLogger(UserDeletedListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserDeleted(UserDeleted event) {
        logger.info("User deleted: userId= {}, deletionDate= {}", event.getUserId(), event.getTimeStamp());

        // Create a DataChangeLog instance
        DataChangeLog dataChangeLog = new DataChangeLog();
        dataChangeLog.setUserId(event.getUserId().toString());
        dataChangeLog.setEntityId(event.getUserId().toString());
        dataChangeLog.setEntity("User");
        dataChangeLog.setAction("DELETE");
        dataChangeLog.setChanges(Map.of(
                "status", Map.of("before", event.getBefore(), "after", "DELETED"),
                "QR", Map.of("before", "ACTIVE", "after", "DELETED")));
        
        auditService.saveWebLog(dataChangeLog);
    }
}
