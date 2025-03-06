package io.github.sibmaks.spring.jfr.report.connections;

import io.github.sibmaks.spring.jfr.dto.view.connections.Connection;
import io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionException;
import io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionsReport;
import io.github.sibmaks.spring.jfr.event.reading.api.pool.jdbc.connection.ConnectionRequestedRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.pool.jdbc.connection.action.ConnectionActionFailedRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.pool.jdbc.connection.action.ConnectionActionRequestedRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.pool.jdbc.connection.action.ConnectionActionSucceedRecordedEvent;
import io.github.sibmaks.spring.jfr.report.connections.dto.ConnectionDto;
import io.github.sibmaks.spring.jfr.report.connections.dto.ConnectionEventDto;
import io.github.sibmaks.spring.jfr.report.connections.dto.ContextDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Component
public class ConnectionsReportCreator {
    private final Map<String, ContextDto> contexts;
    private final Map<String, ConnectionDto> connections;

    public ConnectionsReportCreator() {
        this.contexts = new HashMap<>();
        this.connections = new HashMap<>();
    }

    @EventListener
    public void onConnectionRequested(ConnectionRequestedRecordedEvent event) {
        try {
            var contextId = event.getContextId();
            var context = contexts.computeIfAbsent(contextId, it -> new ContextDto());

            var poolId = event.getPoolId();
            var pool = context.get(poolId);

            var connectionId = event.getConnectionId();
            var connection = pool.getOrCreate(connectionId);
            this.connections.putIfAbsent(connectionId, connection);

            var startTime = event.getStartTime();
            var connectionEvent = ConnectionEventDto.builder()
                    .index(event.getActionIndex())
                    .action(event.getConnectionAction())
                    .startedAt(startTime.toEpochMilli())
                    .finishedAt(-1)
                    .build();
            connection.addEvent(connectionEvent);
        } catch (Exception e) {
            log.error("ConnectionRequestedRecordedEvent processing error", e);
        }
    }

    @EventListener
    public void onConnectionActionRequested(ConnectionActionRequestedRecordedEvent event) {
        try {
            var connection = getConnectionDto(event.getConnectionId());
            if (connection == null) return;

            var startTime = event.getStartTime();
            var connectionEvent = ConnectionEventDto.builder()
                    .index(event.getActionIndex())
                    .action(event.getConnectionAction())
                    .startedAt(startTime.toEpochMilli())
                    .finishedAt(-1)
                    .build();
            connection.addEvent(connectionEvent);
        } catch (Exception e) {
            log.error("ConnectionActionRequestedRecordedEvent processing error", e);
        }
    }

    @EventListener
    public void onConnectionActionSucceed(ConnectionActionSucceedRecordedEvent event) {
        try {
            var connection = getConnectionDto(event.getConnectionId());
            if (connection == null) return;

            var startTime = event.getStartTime();
            var connectionEvent = ConnectionEventDto.builder()
                    .index(event.getActionIndex())
                    .finishedAt(startTime.toEpochMilli())
                    .build();
            connection.addEvent(connectionEvent);
        } catch (Exception e) {
            log.error("ConnectionActionSucceedRecordedEvent processing error", e);
        }
    }

    @EventListener
    public void onConnectionActionFailed(ConnectionActionFailedRecordedEvent event) {
        try {
            var connection = getConnectionDto(event.getConnectionId());
            if (connection == null) return;

            var connectionException = ConnectionException.builder()
                    .type(event.getExceptionClass())
                    .message(event.getExceptionMessage())
                    .build();

            var startTime = event.getStartTime();
            var connectionEvent = ConnectionEventDto.builder()
                    .index(event.getActionIndex())
                    .exception(connectionException)
                    .finishedAt(startTime.toEpochMilli())
                    .build();
            connection.addEvent(connectionEvent);
        } catch (Exception e) {
            log.error("ConnectionActionFailedRecordedEvent processing error", e);
        }
    }

    private ConnectionDto getConnectionDto(String connectionId) {
        var connection = this.connections.get(connectionId);
        if (connection == null) {
            log.warn("Connection {} not found", connectionId);
            return null;
        }
        return connection;
    }

    public ConnectionsReport get() {
        var contextMap = new HashMap<String, Map<String, List<Connection>>>();

        for (var entry : this.contexts.entrySet()) {
            var key = entry.getKey();
            var context = entry.getValue();
            var pool = context.toPoolMap();
            contextMap.put(key, pool);
        }

        return ConnectionsReport.builder()
                .contexts(contextMap)
                .build();
    }

}
