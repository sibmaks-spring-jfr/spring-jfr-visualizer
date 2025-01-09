package io.github.sibmaks.spring.jfr.dto.recorded.common;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.event.api.common.InvocationExecutedFact;
import io.github.sibmaks.spring.jfr.event.api.common.InvocationFailedFact;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public interface InvocationFailedRecordedEvent extends InvocationFailedFact, RecordedData {
}
