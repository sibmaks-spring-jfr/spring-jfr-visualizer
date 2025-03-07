package io.github.sibmaks.spring.jfr.dto.view.connections;

import io.github.sibmaks.spring.jfr.event.api.pool.jdbc.connection.action.ConnectionAction;
import lombok.Builder;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record ConnectionEvent(
        long index,
        ConnectionAction action,
        ConnectionException exception,
        long startedAt,
        long finishedAt,
        String threadName,
        Integer transactionIsolation
) {

}
