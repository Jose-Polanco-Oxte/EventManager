package jpolanco.applicationcore.shared.application.utils;

import java.util.List;

/**
 * Simple EventBus interface for publishing events.
 *
 * @param <E> the type of event
 */
public interface EventBus<E> {
    void publish(E event);

    void publishAll(List<E> events);
}