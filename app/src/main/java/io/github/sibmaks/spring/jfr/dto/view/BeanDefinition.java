package io.github.sibmaks.spring.jfr.dto.view;

import io.github.sibmaks.spring.jfr.event.api.bean.BeanDefinitionRegisteredFact;
import io.github.sibmaks.spring.jfr.event.core.converter.DependencyConverter;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
public class BeanDefinition {
    private final String contextId;
    private final String scope;
    private final String beanClassName;
    private final String beanName;
    private final String primary;
    private final String[] dependencies;
    private final boolean generated;

    public BeanDefinition(BeanDefinitionRegisteredFact fact) {
        this.contextId = fact.getContextId();
        this.scope = fact.getScope();
        this.beanClassName = fact.getBeanClassName();
        this.beanName = fact.getBeanName();
        this.primary = fact.getPrimary();
        this.dependencies = DependencyConverter.convert(fact.getDependencies());
        this.generated = fact.isGenerated();
    }
}
