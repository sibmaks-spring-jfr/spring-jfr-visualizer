package io.github.sibmaks.spring.jfr.dto.view.calls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
@Builder
@AllArgsConstructor
public class CallTrace {
    private final String contextId;
    private final String correlationId;
    private final String invocationId;
    private final boolean success;
    private final InvocationType type;
    private final Instant startTime;
    private final Instant endTime;
    private final Map<String, String> parameters;
    private final List<CallTrace> children;
}
