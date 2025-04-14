package io.github.sibmaks.spring.jfr.report;

import io.github.sibmaks.spring.jfr.bus.SubscribeTo;
import io.github.sibmaks.spring.jfr.dto.protobuf.processing.Event;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeanDefinition;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeanInitialized;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeansReport;
import io.github.sibmaks.spring.jfr.event.api.bean.Stereotype;
import io.github.sibmaks.spring.jfr.event.core.converter.ArrayConverter;
import io.github.sibmaks.spring.jfr.event.recording.bean.*;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BeansReportCreator {
    private final StringConstantRegistry stringConstantRegistry;
    private final Map<Integer, Map<String, BeanDefinition.BeanDefinitionBuilder>> beanDefinitions = new HashMap<>();
    private final Map<String, Map<String, Long>> preConstructed = new HashMap<>();
    private final List<BeanInitialized> initializedBeans = new ArrayList<>();

    public BeansReport create() {
        var beanDefinitions = this.beanDefinitions.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                it -> mapBeanDefinitions(it.getValue())
                        )
                );

        return BeansReport.builder()
                .beanDefinitions(beanDefinitions)
                .beans(initializedBeans)
                .build();
    }

    private List<BeanDefinition> mapBeanDefinitions(Map<String, BeanDefinition.BeanDefinitionBuilder> value) {
        return value.values()
                .stream()
                .map(BeanDefinition.BeanDefinitionBuilder::build)
                .toList();
    }

    @SubscribeTo(BeanDefinitionRegisteredEvent.class)
    public void onBeanDefinition(Event event) {
        var beanDefinition = getBeanDefinitionBuilder(event);

        var contextStringId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));
        var contextBeanDefinitions = beanDefinitions.computeIfAbsent(contextStringId, k -> new HashMap<>());
        contextBeanDefinitions.put(event.getStringFieldsOrThrow("beanName"), beanDefinition);
    }

    private BeanDefinition.BeanDefinitionBuilder getBeanDefinitionBuilder(Event event) {
        return BeanDefinition.builder()
                .scope(stringConstantRegistry.getOrRegister(event.getStringFieldsOrDefault("scope", null)))
                .className(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("beanClassName")))
                .name(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("beanName")))
                .primary(stringConstantRegistry.getOrRegister(event.getStringFieldsOrDefault("primary", null)))
                .dependencies(
                        Arrays.stream(ArrayConverter.convert(event.getStringFieldsOrDefault("dependencies", null)))
                                .map(stringConstantRegistry::getOrRegister)
                                .collect(Collectors.toCollection(TreeSet::new))
                )
                .stereotype(
                        stringConstantRegistry.getOrRegister(
                                Optional.ofNullable(event.getStringFieldsOrDefault("stereotype", null))
                                        .map(Stereotype::valueOf)
                                        .orElse(Stereotype.UNKNOWN)
                                        .name()
                        )
                )
                .generated(event.getBoolFieldsOrThrow("generated"));
    }

    @SubscribeTo(MergedBeanDefinitionRegisteredEvent.class)
    public void onMergedBeanDefinition(Event event) {
        var contextStringId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));
        var contextBeanDefinitions = beanDefinitions.computeIfAbsent(contextStringId, k -> new HashMap<>());
        var beanName = event.getStringFieldsOrThrow("beanName");
        var existedBeanDefinition = contextBeanDefinitions.get(beanName);
        if (existedBeanDefinition != null) {
            existedBeanDefinition.patch(stringConstantRegistry, event);
        } else {
            var beanDefinition = getBeanDefinitionBuilder(event);
            contextBeanDefinitions.put(beanName, beanDefinition);
        }
    }

    @SubscribeTo(ResolveBeanDependencyEvent.class)
    public void onResolveBeanDependency(Event event) {
        var contextStringId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));
        var contextBeanDefinitions = beanDefinitions.get(contextStringId);
        if (contextBeanDefinitions == null) {
            return;
        }
        var beanName = event.getStringFieldsOrThrow("dependentBeanName");
        var existedBeanDefinition = contextBeanDefinitions.get(beanName);
        if (existedBeanDefinition != null) {
            var dependencyBeanName = event.getStringFieldsOrThrow("dependencyBeanName");
            existedBeanDefinition.patch(stringConstantRegistry.getOrRegister(dependencyBeanName));
        }
    }

    @SubscribeTo(PostProcessBeforeInitializationEvent.class)
    public void onPostProcessBeforeInitialization(Event event) {
        var contextId = event.getStringFieldsOrThrow("contextId");
        var beanName = event.getStringFieldsOrThrow("beanName");
        var startTime = event.getStartTime();
        preConstructed.computeIfAbsent(contextId, k -> new HashMap<>())
                .put(beanName, startTime);
    }

    @SubscribeTo(PostProcessAfterInitializationEvent.class)
    public void onPostProcessAfterInitialization(Event event) {
        var contextId = event.getStringFieldsOrThrow("contextId");
        var beanName = event.getStringFieldsOrThrow("beanName");
        var startTime = Optional.ofNullable(preConstructed.get(contextId))
                .map(it -> it.get(beanName))
                .orElse(null);
        var endTime = event.getStartTime();

        if (startTime == null) {
            var beanInitialized = BeanInitialized.builder()
                    .contextId(stringConstantRegistry.getOrRegister(contextId))
                    .beanName(stringConstantRegistry.getOrRegister(beanName))
                    .duration(-1)
                    .postInitializedAt(endTime)
                    .build();
            initializedBeans.add(beanInitialized);
            return;
        }
        var between = endTime - startTime;
        var duration = between / 1_000.;
        var beanInitialized = BeanInitialized.builder()
                .contextId(stringConstantRegistry.getOrRegister(contextId))
                .beanName(stringConstantRegistry.getOrRegister(beanName))
                .duration(duration)
                .preInitializedAt(startTime)
                .postInitializedAt(endTime)
                .build();
        initializedBeans.add(beanInitialized);
    }

}
