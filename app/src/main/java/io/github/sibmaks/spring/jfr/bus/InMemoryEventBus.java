package io.github.sibmaks.spring.jfr.bus;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
public class InMemoryEventBus {
    private final Map<String, List<Consumer<Object>>> subscribers = new ConcurrentHashMap<>();

    public void publish(String topic, Object event) {
        var handlers = subscribers.getOrDefault(topic, List.of());
        for (var handler : handlers) {
            handler.accept(event);
        }
    }

    public void subscribe(String topic, Consumer<Object> handler) {
        subscribers.computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>()).add(handler);
    }
}
