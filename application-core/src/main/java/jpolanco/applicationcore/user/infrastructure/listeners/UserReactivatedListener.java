package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.DataChangeLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.audit.utils.AuditAux;
import jpolanco.applicationcore.user.domain.events.UserReactivated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReactivatedListener {

    private static final Logger log = LoggerFactory.getLogger(UserReactivatedListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserReactivated(UserReactivated event) {
        log.info("User reactivated: userId= {}, timeStamp= {}", event.getUserId(), event.getTimeStamp());

        // Create a DataChangeLog instance
        DataChangeLog dataChangeLog = new DataChangeLog();
        dataChangeLog.setUserId(event.getUserId().toString());
        dataChangeLog.setEntityId(event.getUserId().toString());
        dataChangeLog.setEntity("User");
        dataChangeLog.setAction("REACTIVATE");
        dataChangeLog.setChanges(AuditAux.buildChange("status", event.getBefore(), event.getAfter()));
        
        auditService.saveWebLog(dataChangeLog);
    }
}