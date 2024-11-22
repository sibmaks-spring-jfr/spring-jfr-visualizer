package io.github.sibmaks.spring.jfr.dto.recorded.bean;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.event.api.bean.PostProcessAfterInitializationFact;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public interface PostProcessAfterInitializationRecordedEvent extends PostProcessAfterInitializationFact, RecordedData {
}
