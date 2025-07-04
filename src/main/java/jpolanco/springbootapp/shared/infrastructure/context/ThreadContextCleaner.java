package jpolanco.springbootapp.shared.infrastructure.context;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

@Component
public class ThreadContextCleaner {

    private final ThreadLocalAggregateContext<?, ?> threadLocalAggregateContext;

    public ThreadContextCleaner(ThreadLocalAggregateContext<?, ?> threadLocalAggregateContext) {
        this.threadLocalAggregateContext = threadLocalAggregateContext;
    }

    @EventListener(RequestHandledEvent.class)
    public void clearContext() {
        threadLocalAggregateContext.clear();
    }
}
