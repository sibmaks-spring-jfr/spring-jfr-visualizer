package io.github.sibmaks.spring.jfr.dto.recorded.bean;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.event.api.bean.BeanDefinitionRegisteredFact;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public interface BeanDefinitionRegisteredRecordedEvent extends BeanDefinitionRegisteredFact, RecordedData {
}
