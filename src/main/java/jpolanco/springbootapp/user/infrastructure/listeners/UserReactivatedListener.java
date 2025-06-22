package jpolanco.springbootapp.user.infrastructure.listeners;


import jpolanco.springbootapp.user.domain.domain_events.UserReactivated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserReactivatedListener {
    private static final Logger log = LoggerFactory.getLogger(UserReactivatedListener.class);

    @EventListener
    public void handleUserReactivated(UserReactivated event) {
        log.info("User reactivated: userId= {}, timeStamp= {}",
                 event.getUserId(), event.getTimeStamp());
        // Additional logic can be added here if needed
    }
}