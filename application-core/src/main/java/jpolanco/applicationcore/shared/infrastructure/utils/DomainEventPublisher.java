package jpolanco.applicationcore.shared.infrastructure.utils;

import jpolanco.applicationcore.shared.application.utils.EventBus;
import jpolanco.applicationcore.shared.domain.notify.EventNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DomainEventPublisher implements EventBus<EventNotification> {

    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(EventNotification event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishAll(List<EventNotification> events) {
        events.forEach(publisher::publishEvent);
    }
}
