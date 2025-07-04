package jpolanco.springbootapp.user.infrastructure.listeners;


import jpolanco.springbootapp.shared.infrastructure.auditory.persistence.AuditoryPersistenceImpl;
import jpolanco.springbootapp.user.domain.domainevents.UserReactivated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReactivatedListener {

    private final AuditoryPersistenceImpl auditoryPersistenceImpl;

    private static final Logger log = LoggerFactory.getLogger(UserReactivatedListener.class);

    @EventListener
    public void handleUserReactivated(UserReactivated event) {
        log.info("User reactivated: userId= {}, timeStamp= {}",
                 event.getUserId(), event.getTimeStamp());
        auditoryPersistenceImpl.save(event);
        // Additional logic can be added here if needed
    }
}