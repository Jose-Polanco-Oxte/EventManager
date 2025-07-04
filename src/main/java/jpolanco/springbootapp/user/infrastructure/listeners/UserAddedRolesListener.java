package jpolanco.springbootapp.user.infrastructure.listeners;


import jpolanco.springbootapp.shared.infrastructure.auditory.persistence.AuditoryPersistenceImpl;
import jpolanco.springbootapp.user.domain.domainevents.UserAddedRoles;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAddedRolesListener {

    private final AuditoryPersistenceImpl auditoryPersistenceImpl;

    private static final Logger log = LoggerFactory.getLogger(UserAddedRolesListener.class);

    @EventListener
    public void handleUserAddedRoles(UserAddedRoles event) {
        log.info("User added roles: userId= {}, roles= {}, timeStamp= {}",
                 event.getUserId(), event.getRoles(), event.getTimeStamp());
        auditoryPersistenceImpl.save(event);
    }
}