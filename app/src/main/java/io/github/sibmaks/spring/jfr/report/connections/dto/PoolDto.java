package io.github.sibmaks.spring.jfr.report.connections.dto;

import io.github.sibmaks.spring.jfr.dto.view.connections.Connection;
import io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionEvent;
import lombok.Getter;

import java.util.*;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
public final class PoolDto {
    private final Map<String, ConnectionDto> connections;

    public PoolDto() {
        this.connections = new HashMap<>();
    }

    public ConnectionDto getOrCreate(String connectionId) {
        return connections.computeIfAbsent(connectionId, this::createConnection);
    }

    private ConnectionDto createConnection(String it) {
        return ConnectionDto.builder()
                .id(it)
                .events(new TreeMap<>())
                .build();
    }

    public List<Connection> toConnections() {
        return this.connections.values()
                .stream()
                .map(it -> Connection.builder()
                        .id(it.getId())
                        .duration(it.getDuration())
                        .events(mapEvents(it.getEvents()))
                        .hasExceptions(it.isHasExceptions())
                        .build()
                )
                .toList();
    }

    private List<ConnectionEvent> mapEvents(NavigableMap<Long, ConnectionEventDto> events) {
        return events.entrySet()
                .stream()
                .map(it -> {
                    var value = it.getValue();
                    return ConnectionEvent.builder()
                            .index(it.getKey())
                            .action(value.getAction())
                            .exception(value.getException())
                            .startedAt(value.getStartedAt())
                            .finishedAt(value.getFinishedAt())
                            .threadName(value.getThreadName())
                            .transactionIsolation(value.getTransactionIsolation())
                            .build();
                })
                .toList();
    }
}
