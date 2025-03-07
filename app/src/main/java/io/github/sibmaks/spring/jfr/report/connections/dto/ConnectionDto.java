package io.github.sibmaks.spring.jfr.report.connections.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.NavigableMap;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
public final class ConnectionDto {
    private final NavigableMap<Long, ConnectionEventDto> events;
    private String id;
    private boolean hasExceptions;
    private Integer transactionIsolation;

    public static <T> void mergeParameter(
            String name,
            T value,
            Predicate<T> isValid,
            Supplier<T> existedGetter,
            Consumer<T> setter
    ) {
        if (!isValid.test(value)) {
            return;
        }
        var existed = existedGetter.get();
        if (isValid.test(existed) && Objects.equals(existed, value)) {
            log.warn(
                    "Merge conflict, {}: {}, not set to: {}",
                    name, value, existed
            );
        } else {
            setter.accept(value);
        }
    }

    public void addEvent(ConnectionEventDto event) {
        this.hasExceptions |= event.getException() != null;
        var index = event.getIndex();
        var existed = this.events.get(index);
        if (existed == null) {
            this.events.put(index, event);
            return;
        }
        mergeParameter("startedAt", event.getStartedAt(), it -> it > 0, existed::getStartedAt, existed::setStartedAt);
        mergeParameter("finishedAt", event.getFinishedAt(), it -> it > 0, existed::getFinishedAt, existed::setFinishedAt);
        mergeParameter("action", event.getAction(), Objects::nonNull, existed::getAction, existed::setAction);
        mergeParameter("exception", event.getException(), Objects::nonNull, existed::getException, existed::setException);
        mergeParameter("threadName", event.getThreadName(), Objects::nonNull, existed::getThreadName, existed::setThreadName);
    }

    public long getDuration() {
        var first = events.firstEntry();
        var firstValue = first.getValue();
        var begin = firstValue.getStartedAt() < 0 ? Math.max(0, firstValue.getFinishedAt()) : firstValue.getStartedAt();

        var last = events.lastEntry();
        var lastValue = last.getValue();
        var finished = lastValue.getFinishedAt() < 0 ? Math.max(0, lastValue.getStartedAt()) : lastValue.getFinishedAt();

        return finished - begin;
    }
}
