package io.github.sibmaks.spring.jfr.dto.view.beans;

import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record BeansReport(Map<Long, List<BeanDefinition>> beanDefinitions, List<BeanInitialized> beans) {
}
