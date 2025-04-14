package io.github.sibmaks.spring.jfr.dto.view.beans;

import io.github.sibmaks.spring.jfr.dto.protobuf.processing.Event;
import io.github.sibmaks.spring.jfr.event.core.converter.ArrayConverter;
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
    private final int scope;
    private final int className;
    private final int name;
    private final int primary;
    private final SortedSet<Integer> dependencies;
    private final int stereotype;
    private final boolean generated;

    public static BeanDefinitionBuilder builder() {
        return new BeanDefinitionBuilder();
    }

    public static class BeanDefinitionBuilder {
        private int scope;
        private int className;
        private int name;
        private int primary;
        private SortedSet<Integer> dependencies;
        private int stereotype;
        private boolean generated;

        BeanDefinitionBuilder() {
        }

        public BeanDefinitionBuilder scope(int scope) {
            this.scope = scope;
            return this;
        }

        public BeanDefinitionBuilder className(int className) {
            this.className = className;
            return this;
        }

        public BeanDefinitionBuilder name(int name) {
            this.name = name;
            return this;
        }

        public BeanDefinitionBuilder primary(int primary) {
            this.primary = primary;
            return this;
        }

        public BeanDefinitionBuilder dependencies(SortedSet<Integer> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public BeanDefinitionBuilder stereotype(int stereotype) {
            this.stereotype = stereotype;
            return this;
        }

        public BeanDefinitionBuilder generated(boolean generated) {
            this.generated = generated;
            return this;
        }

        public BeanDefinitionBuilder patch(
                StringConstantRegistry stringConstantRegistry,
                Event fact
        ) {
            if (scope != -1) {
                scope = stringConstantRegistry.getOrRegister(fact.getStringFieldsOrDefault("scope", null));
            }
            if (className != -1) {
                className = stringConstantRegistry.getOrRegister(fact.getStringFieldsOrDefault("beanClassName", null));
            }
            if (name != -1) {
                name = stringConstantRegistry.getOrRegister(fact.getStringFieldsOrThrow("beanName"));
            }
            if (primary != -1) {
                primary = stringConstantRegistry.getOrRegister(fact.getStringFieldsOrDefault("primary", null));
            }
            if (stereotype != -1) {
                int newStereotype = Optional.ofNullable(fact.getStringFieldsOrDefault("stereotype", null))
                        .map(stringConstantRegistry::getOrRegister)
                        .orElse(stereotype);
                if (stereotype != newStereotype && newStereotype != stringConstantRegistry.getOrRegister("UNKNOWN")) {
                    stereotype = newStereotype;
                }
            }

            var mergedDependencies = Arrays.stream(ArrayConverter.convert(fact.getStringFieldsOrDefault("dependencies", null)))
                    .map(stringConstantRegistry::getOrRegister)
                    .toList();
            dependencies.addAll(mergedDependencies);

            return this;
        }

        public BeanDefinitionBuilder patch(int dependency) {
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
