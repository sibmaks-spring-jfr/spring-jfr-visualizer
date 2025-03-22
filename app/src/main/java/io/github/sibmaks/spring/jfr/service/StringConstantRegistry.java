package io.github.sibmaks.spring.jfr.service;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Service
public class StringConstantRegistry {
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<String, Integer> constants;

    public StringConstantRegistry() {
        constants = new ConcurrentHashMap<>();
    }

    public int getOrRegister(String constantName) {
        if (constantName == null) {
            return -1;
        }
        return constants.computeIfAbsent(constantName, it -> counter.getAndIncrement());
    }

    public List<String> getConstants() {
        return constants.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();
    }
}
