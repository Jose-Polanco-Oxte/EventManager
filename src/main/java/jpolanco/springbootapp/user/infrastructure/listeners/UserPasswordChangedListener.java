package jpolanco.springbootapp.user.infrastructure.listeners;

import jpolanco.springbootapp.user.domain.domain_events.UserPasswordChanged;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPasswordChangedListener {
    private static final Logger logger = LoggerFactory.getLogger(UserPasswordChangedListener.class);

    @EventListener
    public void handleUserPasswordChanged(UserPasswordChanged event) {
        logger.info("User password changed: userId= {}, changeDate= {}", event.getUserId(), event.getTimeStamp());
        // Here you can add additional logic, such as sending a notification email
        // emailService.sendPasswordChangeNotification(event.getEmail());
    }
}
