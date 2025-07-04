package jpolanco.springbootapp.user.infrastructure.listeners;

import jpolanco.springbootapp.shared.infrastructure.auditory.persistence.AuditoryPersistenceImpl;
import jpolanco.springbootapp.user.domain.domainevents.UserEmailChanged;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEmailChangedListener {

    private final AuditoryPersistenceImpl auditoryPersistenceImpl;

    private static final Logger logger = LoggerFactory.getLogger(UserEmailChangedListener.class);

    @EventListener
    public void handleUserEmailChanged(UserEmailChanged event) {
        logger.info("User email changed: userId= {}, oldEmail= {}, newEmail= {}, changeDate= {}",
                    event.getUserId(), event.getOldEmail(), event.getNewEmail(), event.getTimeStamp());
        // Additional logic for handling userId email change can be added here
        auditoryPersistenceImpl.save(event);
    }
}
