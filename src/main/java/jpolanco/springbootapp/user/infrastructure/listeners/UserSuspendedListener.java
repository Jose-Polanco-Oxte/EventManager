package jpolanco.springbootapp.user.infrastructure.listeners;

import jpolanco.springbootapp.shared.infrastructure.auditory.persistence.AuditoryPersistenceImpl;
import jpolanco.springbootapp.user.domain.domainevents.UserSuspended;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSuspendedListener {

    private final AuditoryPersistenceImpl auditoryPersistenceImpl;

    private static final Logger logger = LoggerFactory.getLogger(UserSuspendedListener.class);

    @EventListener
    public void handleUserSuspended(UserSuspended event) {
        logger.info("User suspended: userId= {}, suspensionDate= {}", event.getUserId(), event.getTimeStamp());
        auditoryPersistenceImpl.save(event);
        // Additional logic for handling userId suspension can be added here
    }
}
