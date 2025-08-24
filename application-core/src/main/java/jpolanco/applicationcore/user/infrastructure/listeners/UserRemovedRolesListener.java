package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.DataChangeLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.audit.utils.AuditAux;
import jpolanco.applicationcore.user.domain.events.UserRemovedRoles;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRemovedRolesListener {

    private static final Logger log = LoggerFactory.getLogger(UserRemovedRolesListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserRemovedRoles(UserRemovedRoles event) {
        log.info("User removed roles: userId= {}, roles= {}, timeStamp= {}", event.getUserId(), event.getRoles().toString(), event.getTimeStamp());

        // Create a DataChangeLog instance
        DataChangeLog dataChangeLog = new DataChangeLog();
        dataChangeLog.setUserId(event.getUserId().toString());
        dataChangeLog.setEntityId(event.getUserId().toString());
        dataChangeLog.setEntity("User");
        dataChangeLog.setAction("REMOVE_ROLES");
        dataChangeLog.setChanges(AuditAux.buildChange("roles", event.getBefore(), event.getAfter()));

        auditService.saveWebLog(dataChangeLog);
    }
}