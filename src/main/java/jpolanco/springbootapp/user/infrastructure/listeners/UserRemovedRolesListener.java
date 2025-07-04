package jpolanco.springbootapp.user.infrastructure.listeners;


import jpolanco.springbootapp.shared.infrastructure.auditory.persistence.AuditoryPersistenceImpl;
import jpolanco.springbootapp.user.domain.domainevents.UserRemovedRoles;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRemovedRolesListener {

    private final AuditoryPersistenceImpl auditoryPersistenceImpl;

    private static final Logger log = LoggerFactory.getLogger(UserRemovedRolesListener.class);

    @EventListener
    public void handleUserRemovedRoles(UserRemovedRoles event) {
        log.info("User removed roles: userId= {}, roles= {}, timeStamp= {}",
                 event.getUserId(), event.getRoles().toString(), event.getTimeStamp());
        auditoryPersistenceImpl.save(event);
        // Additional logic can be added here if needed
    }
}