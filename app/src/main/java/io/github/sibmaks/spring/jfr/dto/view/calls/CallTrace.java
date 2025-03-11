package io.github.sibmaks.spring.jfr.dto.view.calls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record CallTrace(
        @JsonIgnore
        long contextId,
        @JsonIgnore
        Long correlationId,
        long invocationId,
        int success,
        long type,
        long startTime,
        long endTime,
        Long threadName,
        long className,
        long methodName,
        Map<Long, Long> details,
        List<CallTrace> children
) {
}
