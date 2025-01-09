package io.github.sibmaks.spring.jfr.dto.view.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
@Builder
@AllArgsConstructor
public class BeansReport {
    private final List<BeanDefinition> beanDefinitions;
    private final List<BeanInitialized> beans;
}
