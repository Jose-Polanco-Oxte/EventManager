package jpolanco.springbootapp.shared.infrastructure.context;

import java.util.Optional;

public interface AggregateContext<D, E> {
    void track(D domain, E entity);
    Optional<E> get(D domain);
    void clear();
}
