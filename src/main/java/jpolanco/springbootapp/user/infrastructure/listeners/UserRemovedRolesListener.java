package jpolanco.springbootapp.user.infrastructure.listeners;


import jpolanco.springbootapp.user.domain.domain_events.UserRemovedRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRemovedRolesListener {
    private static final Logger log = LoggerFactory.getLogger(UserRemovedRolesListener.class);

    @EventListener
    public void handleUserRemovedRoles(UserRemovedRoles event) {
        log.info("User removed roles: userId= {}, roles= {}, timeStamp= {}",
                 event.getUserId(), event.getRoles().toString(), event.getTimeStamp());
        // Additional logic can be added here if needed
    }
}