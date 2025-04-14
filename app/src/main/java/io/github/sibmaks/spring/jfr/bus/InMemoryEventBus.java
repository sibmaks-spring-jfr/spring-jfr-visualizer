package io.github.sibmaks.spring.jfr.bus;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
public class InMemoryEventBus {
    private final Map<Class<?>, List<Consumer<Object>>> subscribers = new ConcurrentHashMap<>();

    public void publish(Class<?> eventType, Object event) {
        for (var entry : subscribers.entrySet()) {
            var key = entry.getKey();
            if (!key.isAssignableFrom(eventType)) {
                continue;
            }
            for (var consumer : entry.getValue()) {
                consumer.accept(event);
            }
        }
    }

    public void subscribe(Class<?> type, Consumer<Object> handler) {
        subscribers.computeIfAbsent(type, t -> new CopyOnWriteArrayList<>()).add(handler);
    }
}
