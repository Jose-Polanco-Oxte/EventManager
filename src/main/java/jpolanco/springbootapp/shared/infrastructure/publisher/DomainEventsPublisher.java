package jpolanco.springbootapp.shared.infrastructure.publisher;

import jpolanco.springbootapp.shared.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DomainEventsPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}
