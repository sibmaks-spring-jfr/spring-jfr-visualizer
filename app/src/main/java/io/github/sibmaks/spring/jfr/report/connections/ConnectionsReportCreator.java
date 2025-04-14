package io.github.sibmaks.spring.jfr.report.connections;

import io.github.sibmaks.spring.jfr.bus.SubscribeTo;
import io.github.sibmaks.spring.jfr.dto.protobuf.processing.Event;
import io.github.sibmaks.spring.jfr.dto.view.connections.Connection;
import io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionException;
import io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionsReport;
import io.github.sibmaks.spring.jfr.event.api.pool.jdbc.connection.action.ConnectionAction;
import io.github.sibmaks.spring.jfr.event.recording.pool.jdbc.connection.ConnectionRequestedEvent;
import io.github.sibmaks.spring.jfr.event.recording.pool.jdbc.connection.ConnectionTransactionLevelSetEvent;
import io.github.sibmaks.spring.jfr.event.recording.pool.jdbc.connection.action.ConnectionActionFailedEvent;
import io.github.sibmaks.spring.jfr.event.recording.pool.jdbc.connection.action.ConnectionActionRequestedEvent;
import io.github.sibmaks.spring.jfr.event.recording.pool.jdbc.connection.action.ConnectionActionSucceedEvent;
import io.github.sibmaks.spring.jfr.report.connections.dto.ConnectionDto;
import io.github.sibmaks.spring.jfr.report.connections.dto.ConnectionEventDto;
import io.github.sibmaks.spring.jfr.report.connections.dto.ContextDto;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.extern.slf4j.Slf4j;
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
    private final Map<Integer, ContextDto> contexts;
    private final Map<String, ConnectionDto> connections;
    private final StringConstantRegistry stringConstantRegistry;

    public ConnectionsReportCreator(StringConstantRegistry stringConstantRegistry) {
        this.contexts = new HashMap<>();
        this.connections = new HashMap<>();
        this.stringConstantRegistry = stringConstantRegistry;
    }

    @SubscribeTo(ConnectionRequestedEvent.class)
    public void onConnectionRequested(Event event) {
        try {
            var contextId = event.getStringFieldsOrThrow("contextId");
            var context = contexts.computeIfAbsent(stringConstantRegistry.getOrRegister(contextId), it -> new ContextDto());

            var poolId = event.getStringFieldsOrThrow("poolId");
            var pool = context.get(stringConstantRegistry.getOrRegister(poolId));

            var connectionId = event.getStringFieldsOrThrow("connectionId");
            var connection = pool.getOrCreate(connectionId);
            this.connections.putIfAbsent(connectionId, connection);

            var startTime = event.getStartTime();
            var connectionEvent = ConnectionEventDto.builder()
                    .index(0)
                    .action(stringConstantRegistry.getOrRegister(ConnectionAction.CREATE.name()))
                    .startedAt(startTime)
                    .finishedAt(-1)
                    .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                    .transactionIsolation(connection.getTransactionIsolation())
                    .build();
            connection.addEvent(connectionEvent);
        } catch (Exception e) {
            log.error("ConnectionRequestedRecordedEvent processing error", e);
        }
    }

    @SubscribeTo(ConnectionActionRequestedEvent.class)
    public void onConnectionActionRequested(Event event) {
        try {
            var connection = getConnectionDto(event.getStringFieldsOrThrow("connectionId"));
            if (connection == null) {
                log.error("ConnectionActionRequestedRecordedEvent processing error, connection not found");
                return;
            }

            var startTime = event.getStartTime();
            var connectionEvent = ConnectionEventDto.builder()
                    .index(event.getInt64FieldsOrThrow("actionIndex"))
                    .action(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("action")))
                    .startedAt(startTime)
                    .finishedAt(-1)
                    .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                    .transactionIsolation(connection.getTransactionIsolation())
                    .build();
            connection.addEvent(connectionEvent);
        } catch (Exception e) {
            log.error("ConnectionActionRequestedRecordedEvent processing error", e);
        }
    }

    @SubscribeTo(ConnectionActionSucceedEvent.class)
    public void onConnectionActionSucceed(Event event) {
        try {
            var connection = getConnectionDto(event.getStringFieldsOrThrow("connectionId"));
            if (connection == null) {
                log.error("ConnectionActionSucceedRecordedEvent processing error, connection not found");
                return;
            }

            var startTime = event.getStartTime();
            var connectionEvent = ConnectionEventDto.builder()
                    .index(event.getInt64FieldsOrThrow("actionIndex"))
                    .finishedAt(startTime)
                    .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                    .transactionIsolation(connection.getTransactionIsolation())
                    .build();
            connection.addEvent(connectionEvent);
        } catch (Exception e) {
            log.error("ConnectionActionSucceedRecordedEvent processing error", e);
        }
    }

    @SubscribeTo(ConnectionActionFailedEvent.class)
    public void onConnectionActionFailed(Event event) {
        try {
            var connection = getConnectionDto(event.getStringFieldsOrThrow("connectionId"));
            if (connection == null) {
                log.error("ConnectionActionFailedRecordedEvent processing error, connection not found");
                return;
            }

            var connectionException = ConnectionException.builder()
                    .type(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("exceptionClass")))
                    .message(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("exceptionMessage")))
                    .build();

            var startTime = event.getStartTime();
            var connectionEvent = ConnectionEventDto.builder()
                    .index(event.getInt64FieldsOrThrow("actionIndex"))
                    .exception(connectionException)
                    .finishedAt(startTime)
                    .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                    .transactionIsolation(connection.getTransactionIsolation())
                    .build();
            connection.addEvent(connectionEvent);
        } catch (Exception e) {
            log.error("ConnectionActionFailedRecordedEvent processing error", e);
        }
    }

    @SubscribeTo(ConnectionTransactionLevelSetEvent.class)
    public void onConnectionTransactionLevelSet(Event event) {
        try {
            var connection = getConnectionDto(event.getStringFieldsOrThrow("connectionId"));
            if (connection == null) {
                log.error("ConnectionTransactionLevelSetRecordedEvent processing error, connection not found");
                return;
            }
            var transactionLevel = event.getInt32FieldsOrThrow("transactionLevel");
            connection.setTransactionIsolation(transactionLevel);
        } catch (Exception e) {
            log.error("ConnectionTransactionLevelSetRecordedEvent processing error", e);
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
        var contextMap = new HashMap<Integer, Map<Integer, List<Connection>>>();

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
