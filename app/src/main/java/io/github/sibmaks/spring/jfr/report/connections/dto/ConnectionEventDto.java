package io.github.sibmaks.spring.jfr.report.connections.dto;

import io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ConnectionEventDto {
    private final long index;
    private long action;
    private ConnectionException exception;
    private long startedAt;
    private long finishedAt;
    private int threadName;
    private Integer transactionIsolation;

}
