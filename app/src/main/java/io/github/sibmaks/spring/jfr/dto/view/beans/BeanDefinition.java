package io.github.sibmaks.spring.jfr.dto.view.beans;

import io.github.sibmaks.spring.jfr.event.api.bean.BeanDefinitionRegisteredFact;
import io.github.sibmaks.spring.jfr.event.api.bean.MergedBeanDefinitionRegisteredFact;
import io.github.sibmaks.spring.jfr.event.api.bean.Stereotype;
import io.github.sibmaks.spring.jfr.event.core.converter.DependencyConverter;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
public class BeanDefinition {
    private String scope;
    private String beanClassName;
    private String beanName;
    private String primary;
    private String[] dependencies;
    private Stereotype stereotype;
    private final boolean generated;

    public BeanDefinition(BeanDefinitionRegisteredFact fact) {
        this.scope = fact.getScope();
        this.beanClassName = fact.getBeanClassName();
        this.beanName = fact.getBeanName();
        this.primary = fact.getPrimary();
        this.dependencies = DependencyConverter.convert(fact.getDependencies());
        this.stereotype = Optional.ofNullable(fact.getStereotype())
                .map(Stereotype::valueOf)
                .orElse(Stereotype.UNKNOWN);
        this.generated = fact.isGenerated();
    }

    public void patch(MergedBeanDefinitionRegisteredFact fact) {
        if (!StringUtils.hasText(scope)) {
            scope = fact.getScope();
        }
        if (!StringUtils.hasText(beanClassName)) {
            beanClassName = fact.getBeanClassName();
        }
        if (!StringUtils.hasText(beanName)) {
            beanName = fact.getBeanName();
        }
        if (!StringUtils.hasText(primary)) {
            primary = fact.getPrimary();
        }
        if (stereotype == null) {
            var newStereotype = Optional.ofNullable(fact.getStereotype())
                    .map(Stereotype::valueOf)
                    .orElse(stereotype);
            if (stereotype != newStereotype && newStereotype != Stereotype.UNKNOWN) {
                stereotype = newStereotype;
            }
        }
        patchDependencies(fact);
    }

    private void patchDependencies(MergedBeanDefinitionRegisteredFact fact) {
        var mergedDependencies = DependencyConverter.convert(fact.getDependencies());
        var newDependencies = new HashSet<>(this.dependencies.length + mergedDependencies.length);
        newDependencies.addAll(List.of(this.dependencies));
        newDependencies.addAll(List.of(mergedDependencies));
        this.dependencies = newDependencies.toArray(new String[newDependencies.size()]);
    }
}
