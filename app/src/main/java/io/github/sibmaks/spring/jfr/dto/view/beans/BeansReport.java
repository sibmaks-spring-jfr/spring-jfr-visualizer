package io.github.sibmaks.spring.jfr.dto.view.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
@Builder
@AllArgsConstructor
public class BeansReport {
    private final Map<String, List<BeanDefinition>> beanDefinitions;
    private final List<BeanInitialized> beans;
}
