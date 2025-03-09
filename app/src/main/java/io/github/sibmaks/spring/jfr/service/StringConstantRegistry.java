package io.github.sibmaks.spring.jfr.service;


import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Service
public class StringConstantRegistry {
    private final AtomicLong counter = new AtomicLong();
    private final Map<String, Long> constants;

    public StringConstantRegistry() {
        constants = new ConcurrentHashMap<>();
    }

    public long getOrRegister(String constantName) {
        if (constantName == null) {
            return -1;
        }
        return constants.computeIfAbsent(constantName, it -> counter.incrementAndGet());
    }

    public Map<Long, String> getConstants() {
        return constants.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
