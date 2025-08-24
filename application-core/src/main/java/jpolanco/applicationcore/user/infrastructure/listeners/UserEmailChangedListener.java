package jpolanco.applicationcore.user.infrastructure.listeners;

import jpolanco.applicationcore.audit.persistence.DataChangeLog;
import jpolanco.applicationcore.audit.services.interfaces.AuditService;
import jpolanco.applicationcore.user.domain.events.UserEmailChanged;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserEmailChangedListener {

    private static final Logger logger = LoggerFactory.getLogger(UserEmailChangedListener.class);
    private final AuditService auditService;

    @EventListener
    public void handleUserEmailChanged(UserEmailChanged event) {
        logger.info("User email changed: userId= {}, oldEmail= {}, newEmail= {}, changeDate= {}",
                event.getUserId(), event.getBefore(), event.getAfter(), event.getTimeStamp());

        // Create a DataChangeLog instance
        DataChangeLog dataChangeLog = new DataChangeLog();
        dataChangeLog.setUserId(event.getUserId().toString());
        dataChangeLog.setEntityId(event.getUserId().toString());
        dataChangeLog.setEntity("User");
        dataChangeLog.setAction("EMAIL_CHANGE");
        dataChangeLog.setChanges(Map.of(
                "email", Map.of("before", event.getBefore(), "after", event.getAfter())));

        auditService.saveWebLog(dataChangeLog);
    }
}
