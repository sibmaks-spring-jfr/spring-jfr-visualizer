package io.github.sibmaks.spring.jfr.dto.recorded.service;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.event.api.jpa.JPAMethodCalledFact;
import io.github.sibmaks.spring.jfr.event.api.service.ServiceMethodCalledFact;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public interface ServiceMethodCalledRecordedEvent extends ServiceMethodCalledFact, RecordedData {
}
