package io.github.sibmaks.spring.jfr.dto.view.beans;

import io.github.sibmaks.spring.jfr.event.api.bean.BeanDefinitionRegisteredFact;
import io.github.sibmaks.spring.jfr.event.api.bean.MergedBeanDefinitionRegisteredFact;
import io.github.sibmaks.spring.jfr.event.api.bean.Stereotype;
import io.github.sibmaks.spring.jfr.event.core.converter.DependencyConverter;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
public class BeanDefinition {
    private long scope;
    private long className;
    private long name;
    private long primary;
    private final SortedSet<Long> dependencies;
    private long stereotype;
    private final int generated;

    public BeanDefinition(StringConstantRegistry stringConstantRegistry, BeanDefinitionRegisteredFact fact) {
        this.scope = stringConstantRegistry.getOrRegister(fact.getScope());
        this.className = stringConstantRegistry.getOrRegister(fact.getBeanClassName());
        this.name = stringConstantRegistry.getOrRegister(fact.getBeanName());
        this.primary = stringConstantRegistry.getOrRegister(fact.getPrimary());
        this.dependencies = Arrays.stream(DependencyConverter.convert(fact.getDependencies()))
                .map(stringConstantRegistry::getOrRegister)
                .collect(Collectors.toCollection(TreeSet::new));
        this.stereotype = stringConstantRegistry.getOrRegister(
                Optional.ofNullable(fact.getStereotype())
                        .map(Stereotype::valueOf)
                        .orElse(Stereotype.UNKNOWN)
                        .name()
        );
        this.generated = fact.isGenerated() ? 1 : 0;
    }

    public void patch(StringConstantRegistry stringConstantRegistry, MergedBeanDefinitionRegisteredFact fact) {
        if (scope != -1) {
            scope = stringConstantRegistry.getOrRegister(fact.getScope());
        }
        if (className != -1) {
            className = stringConstantRegistry.getOrRegister(fact.getBeanClassName());
        }
        if (name != -1) {
            name = stringConstantRegistry.getOrRegister(fact.getBeanName());
        }
        if (primary != -1) {
            primary = stringConstantRegistry.getOrRegister(fact.getPrimary());
        }
        if (stereotype != -1) {
            long newStereotype = Optional.ofNullable(fact.getStereotype())
                    .map(stringConstantRegistry::getOrRegister)
                    .orElse(stereotype);
            if (stereotype != newStereotype && newStereotype != stringConstantRegistry.getOrRegister("UNKNOWN")) {
                stereotype = newStereotype;
            }
        }

        var mergedDependencies = Arrays.stream(DependencyConverter.convert(fact.getDependencies()))
                .map(stringConstantRegistry::getOrRegister)
                .toList();
        dependencies.addAll(mergedDependencies);
    }

    public void patch(long dependency) {
        dependencies.add(dependency);
    }
}
