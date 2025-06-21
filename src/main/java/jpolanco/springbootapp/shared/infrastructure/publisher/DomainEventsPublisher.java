package jpolanco.springbootapp.shared.infrastructure.publisher;

import jpolanco.springbootapp.shared.domain.EventNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DomainEventsPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(EventNotification event) {
        publisher.publishEvent(event);
    }

    public void publishAll(List<EventNotification> events) {
        events.forEach(publisher::publishEvent);
        events.clear();
    }
}
