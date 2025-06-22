package jpolanco.springbootapp.user.infrastructure.listeners;


import jpolanco.springbootapp.user.domain.domain_events.UserDeactivated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserDeactivatedListener {
    private static final Logger log = LoggerFactory.getLogger(UserDeactivatedListener.class);

    @EventListener
    public void handleUserDeactivated(UserDeactivated event) {
        log.info("User deactivated: userId= {}, reason= {}, timeStamp= {}",
                 event.getUserId(), event.getReason(), event.getTimeStamp());
    }
}