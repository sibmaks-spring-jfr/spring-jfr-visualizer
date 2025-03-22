package io.github.sibmaks.spring.jfr.utils;

import io.github.sibmaks.spring.jfr.event.reading.api.RecordedData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JavaFlightRecorderUtils {
    public static String getThreadName(RecordedData event) {
        var thread = event.getThread();
        return thread == null ? null : thread.getJavaName();
    }
}
