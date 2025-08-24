package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.DataChangeLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.audit.utils.AuditAux;
import jpolanco.applicationcore.user.domain.events.UserDeactivated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeactivatedListener {

    private static final Logger log = LoggerFactory.getLogger(UserDeactivatedListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserDeactivated(UserDeactivated event) {
        log.info("User deactivated: userId= {}, reason= {}, timeStamp= {}", event.getUserId(), event.getReason(), event.getTimeStamp());

        // Create a DataChangeLog instance
        DataChangeLog dataChangeLog = new DataChangeLog();
        dataChangeLog.setUserId(event.getUserId().toString());
        dataChangeLog.setEntityId(event.getUserId().toString());
        dataChangeLog.setEntity("User");
        dataChangeLog.setAction("DEACTIVATE");
        dataChangeLog.setChanges(AuditAux.buildChange("status", event.getBefore(), event.getAfter(), event.getReason()));
        
        auditService.saveWebLog(dataChangeLog);
    }
}