package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.DataChangeLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.audit.utils.AuditAux;
import jpolanco.applicationcore.user.domain.events.UserSuspended;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSuspendedListener {

    private static final Logger logger = LoggerFactory.getLogger(UserSuspendedListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserSuspended(UserSuspended event) {
        logger.info("User suspended: userId= {}, suspensionDate= {}", event.getUserId(), event.getTimeStamp());

        // Create a DataChangeLog instance
        DataChangeLog dataChangeLog = new DataChangeLog();
        dataChangeLog.setUserId(event.getUserId().toString());
        dataChangeLog.setEntityId(event.getUserId().toString());
        dataChangeLog.setEntity("User");
        dataChangeLog.setAction("SUSPEND");
        dataChangeLog.setChanges(AuditAux.buildChange("suspended", event.getBefore(), event.getAfter(), event.getReason()));
        
        auditService.saveWebLog(dataChangeLog);
    }
}
