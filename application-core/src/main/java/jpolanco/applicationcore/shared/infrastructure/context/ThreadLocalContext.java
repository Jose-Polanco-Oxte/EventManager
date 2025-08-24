package jpolanco.applicationcore.shared.infrastructure.context;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ThreadLocalContext is a utility class that provides a thread-local storage mechanism
 * for storing and retrieving context-specific data during the lifecycle of a request.
 * It uses a ThreadLocal variable to maintain a separate context map for each thread,
 * ensuring that data is isolated between different requests.
 */
@Component
public class ThreadLocalContext {

    private static final ThreadLocal<Map<String, Object>> context = ThreadLocal.withInitial(HashMap::new);

    public <T> void put(String key, T value) {
        context.get().put(key, value);
    }

    // This method retrieves a value from the context map and attempts to cast it to the specified class type.
    // If the value is present and of the correct type, it is returned wrapped in an Optional.
    // If the value is absent or not of the expected type, an empty Optional is returned.
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key, Class<T> clazz) {
        Object value = context.get().get(key);
        if (clazz.isInstance(value)) {
            return Optional.of((T) value);
        }
        return Optional.empty();
    }

    public void clear() {
        context.get().clear();
    }
}
