package io.github.sibmaks.spring.jfr.dto.recorded.jpa;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.event.api.jpa.JPAMethodCalledFact;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public interface JPAMethodCalledRecordedEvent extends JPAMethodCalledFact, RecordedData {
}
