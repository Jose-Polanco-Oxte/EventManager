package jpolanco.springbootapp.user.infrastructure.listeners;


import jpolanco.springbootapp.shared.infrastructure.auditory.persistence.AuditoryPersistenceImpl;
import jpolanco.springbootapp.user.domain.domainevents.UserDeactivated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDeactivatedListener {

    private final AuditoryPersistenceImpl auditoryPersistenceImpl;

    private static final Logger log = LoggerFactory.getLogger(UserDeactivatedListener.class);

    @EventListener
    public void handleUserDeactivated(UserDeactivated event) {
        log.info("User deactivated: userId= {}, reason= {}, timeStamp= {}",
                 event.getUserId(), event.getReason(), event.getTimeStamp());
        auditoryPersistenceImpl.save(event);
    }
}