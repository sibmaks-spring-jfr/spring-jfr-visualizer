package io.github.sibmaks.spring.jfr.dto.recorded.scheduled;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.event.api.scheduled.ScheduledMethodCalledFact;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public interface ScheduledMethodCalledRecordedEvent extends ScheduledMethodCalledFact, RecordedData {
}
