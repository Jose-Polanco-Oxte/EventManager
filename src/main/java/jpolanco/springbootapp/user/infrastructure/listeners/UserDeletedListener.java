package jpolanco.springbootapp.user.infrastructure.listeners;

import jpolanco.springbootapp.shared.infrastructure.auditory.persistence.AuditoryPersistenceImpl;
import jpolanco.springbootapp.user.application.appevents.UserDeleted;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeletedListener {

    private final AuditoryPersistenceImpl auditoryPersistenceImpl;

    private static final Logger logger = LoggerFactory.getLogger(UserDeletedListener.class);

    @EventListener
    public void handleUserDeleted(UserDeleted event) {
        logger.info("User deleted: userId= {}, deletionDate= {}", event.getUserId(), event.getTimeStamp());
        // Additional logic for handling userId deletion can be added here
        auditoryPersistenceImpl.save(event);
    }
}
