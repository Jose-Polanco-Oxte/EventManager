package jpolanco.springbootapp.shared.infrastructure.mappers;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ThreadLocalAggregateContext<D, E> implements AggregateContext<D, E> {

    private final ThreadLocal<Map<D, E>> context = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void track(D domain, E entity) {
        context.get().put(domain, entity);
    }

    @Override
    public Optional<E> get(D domain) {
        return Optional.ofNullable(context.get().get(domain));
    }

    @Override
    public void clear() {
        context.get().clear();
    }
}
