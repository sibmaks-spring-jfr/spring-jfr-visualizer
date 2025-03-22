package io.github.sibmaks.spring.jfr.dto.view.beans;

import io.github.sibmaks.spring.jfr.event.api.bean.MergedBeanDefinitionRegisteredFact;
import io.github.sibmaks.spring.jfr.event.core.converter.DependencyConverter;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.SortedSet;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public class BeanDefinition {
    private final long scope;
    private final long className;
    private final long name;
    private final long primary;
    private final SortedSet<Long> dependencies;
    private final long stereotype;
    private final int generated;

    public static BeanDefinitionBuilder builder() {
        return new BeanDefinitionBuilder();
    }

    public static class BeanDefinitionBuilder {
        private long scope;
        private long className;
        private long name;
        private long primary;
        private SortedSet<Long> dependencies;
        private long stereotype;
        private int generated;

        BeanDefinitionBuilder() {
        }

        public BeanDefinitionBuilder scope(long scope) {
            this.scope = scope;
            return this;
        }

        public BeanDefinitionBuilder className(long className) {
            this.className = className;
            return this;
        }

        public BeanDefinitionBuilder name(long name) {
            this.name = name;
            return this;
        }

        public BeanDefinitionBuilder primary(long primary) {
            this.primary = primary;
            return this;
        }

        public BeanDefinitionBuilder dependencies(SortedSet<Long> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public BeanDefinitionBuilder stereotype(long stereotype) {
            this.stereotype = stereotype;
            return this;
        }

        public BeanDefinitionBuilder generated(int generated) {
            this.generated = generated;
            return this;
        }

        public BeanDefinitionBuilder patch(StringConstantRegistry stringConstantRegistry, MergedBeanDefinitionRegisteredFact fact) {
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

            return this;
        }

        public BeanDefinitionBuilder patch(long dependency) {
            dependencies.add(dependency);
            return this;
        }

        public BeanDefinition build() {
            return new BeanDefinition(
                    this.scope,
                    this.className,
                    this.name,
                    this.primary,
                    this.dependencies,
                    this.stereotype,
                    this.generated
            );
        }

        public String toString() {
            return "BeanDefinition.BeanDefinitionBuilder(scope=" + this.scope + ", className=" + this.className + ", name=" + this.name + ", primary=" + this.primary + ", dependencies=" + this.dependencies + ", stereotype=" + this.stereotype + ", generated=" + this.generated + ")";
        }
    }
}
