package io.github.sibmaks.spring.jfr.dto.view.connections;

import lombok.Builder;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record ConnectionEvent(
        long index,
        long action,
        ConnectionException exception,
        long startedAt,
        long finishedAt,
        long threadName,
        Integer transactionIsolation
) {

}
