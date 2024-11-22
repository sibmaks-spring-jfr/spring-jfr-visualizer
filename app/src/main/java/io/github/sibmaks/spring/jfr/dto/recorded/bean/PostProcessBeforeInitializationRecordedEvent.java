package io.github.sibmaks.spring.jfr.dto.recorded.bean;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.event.api.bean.PostProcessBeforeInitializationFact;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public interface PostProcessBeforeInitializationRecordedEvent extends PostProcessBeforeInitializationFact, RecordedData {
}
