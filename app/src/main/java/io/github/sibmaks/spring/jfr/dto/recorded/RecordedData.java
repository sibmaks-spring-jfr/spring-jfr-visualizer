package io.github.sibmaks.spring.jfr.dto.recorded;

import jdk.jfr.consumer.RecordedThread;

import java.time.Instant;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public interface RecordedData {
    Instant getStartTime();

    RecordedThread getThread();
}
