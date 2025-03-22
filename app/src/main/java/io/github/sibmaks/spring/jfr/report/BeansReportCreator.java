package io.github.sibmaks.spring.jfr.report;

import io.github.sibmaks.spring.jfr.dto.view.beans.BeanDefinition;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeanInitialized;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeansReport;
import io.github.sibmaks.spring.jfr.event.api.bean.BeanDefinitionRegisteredFact;
import io.github.sibmaks.spring.jfr.event.api.bean.Stereotype;
import io.github.sibmaks.spring.jfr.event.core.converter.DependencyConverter;
import io.github.sibmaks.spring.jfr.event.reading.api.bean.PostProcessAfterInitializationRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.bean.PostProcessBeforeInitializationRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.bean.ResolveBeanDependencyRecordedEvent;
import io.github.sibmaks.spring.jfr.event.recording.bean.MergedBeanDefinitionRegisteredEvent;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
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
    private final Map<Long, Map<String, BeanDefinition.BeanDefinitionBuilder>> beanDefinitions = new HashMap<>();
    private final Map<String, Map<String, Instant>> preConstructed = new HashMap<>();
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

    @EventListener
    public void onBeanDefinition(BeanDefinitionRegisteredFact event) {
        var beanDefinition = getBeanDefinitionBuilder(event);

        var contextStringId = stringConstantRegistry.getOrRegister(event.getContextId());
        var contextBeanDefinitions = beanDefinitions.computeIfAbsent(contextStringId, k -> new HashMap<>());
        contextBeanDefinitions.put(event.getBeanName(), beanDefinition);
    }

    private BeanDefinition.BeanDefinitionBuilder getBeanDefinitionBuilder(BeanDefinitionRegisteredFact event) {
        return BeanDefinition.builder()
                .scope(stringConstantRegistry.getOrRegister(event.getScope()))
                .className(stringConstantRegistry.getOrRegister(event.getBeanClassName()))
                .name(stringConstantRegistry.getOrRegister(event.getBeanName()))
                .primary(stringConstantRegistry.getOrRegister(event.getPrimary()))
                .dependencies(
                        Arrays.stream(DependencyConverter.convert(event.getDependencies()))
                                .map(stringConstantRegistry::getOrRegister)
                                .collect(Collectors.toCollection(TreeSet::new))
                )
                .stereotype(
                        stringConstantRegistry.getOrRegister(
                                Optional.ofNullable(event.getStereotype())
                                        .map(Stereotype::valueOf)
                                        .orElse(Stereotype.UNKNOWN)
                                        .name()
                        )
                )
                .generated(event.isGenerated() ? 1 : 0);
    }

    @EventListener
    public void onMergedBeanDefinition(MergedBeanDefinitionRegisteredEvent event) {
        var contextStringId = stringConstantRegistry.getOrRegister(event.getContextId());
        var contextBeanDefinitions = beanDefinitions.computeIfAbsent(contextStringId, k -> new HashMap<>());
        var beanName = event.getBeanName();
        var existedBeanDefinition = contextBeanDefinitions.get(beanName);
        if (existedBeanDefinition != null) {
            existedBeanDefinition.patch(stringConstantRegistry, event);
        } else {
            var beanDefinition = getBeanDefinitionBuilder(event);
            contextBeanDefinitions.put(beanName, beanDefinition);
        }
    }

    @EventListener
    public void onResolveBeanDependency(ResolveBeanDependencyRecordedEvent event) {
        var contextStringId = stringConstantRegistry.getOrRegister(event.getContextId());
        var contextBeanDefinitions = beanDefinitions.get(contextStringId);
        if (contextBeanDefinitions == null) {
            return;
        }
        var beanName = event.getDependentBeanName();
        var existedBeanDefinition = contextBeanDefinitions.get(beanName);
        if (existedBeanDefinition != null) {
            var dependencyBeanName = event.getDependencyBeanName();
            existedBeanDefinition.patch(stringConstantRegistry.getOrRegister(dependencyBeanName));
        }
    }

    @EventListener
    public void onPostProcessBeforeInitialization(PostProcessBeforeInitializationRecordedEvent event) {
        var contextId = event.getContextId();
        var beanName = event.getBeanName();
        var startTime = event.getStartTime();
        preConstructed.computeIfAbsent(contextId, k -> new HashMap<>())
                .put(beanName, startTime);
    }

    @EventListener
    public void onPostProcessAfterInitialization(PostProcessAfterInitializationRecordedEvent event) {
        var contextId = event.getContextId();
        var beanName = event.getBeanName();
        var startTime = Optional.ofNullable(preConstructed.get(contextId))
                .map(it -> it.get(beanName))
                .orElse(null);
        var endTime = event.getStartTime();

        if (startTime == null) {
            var beanInitialized = BeanInitialized.builder()
                    .contextId(stringConstantRegistry.getOrRegister(contextId))
                    .beanName(stringConstantRegistry.getOrRegister(beanName))
                    .duration(-1)
                    .postInitializedAt(endTime.toEpochMilli())
                    .build();
            initializedBeans.add(beanInitialized);
            return;
        }
        var between = Duration.between(startTime, endTime);
        var duration = between.toNanos() / 1_000_000.;
        var beanInitialized = BeanInitialized.builder()
                .contextId(stringConstantRegistry.getOrRegister(contextId))
                .beanName(stringConstantRegistry.getOrRegister(beanName))
                .duration(duration)
                .preInitializedAt(startTime.toEpochMilli())
                .postInitializedAt(endTime.toEpochMilli())
                .build();
        initializedBeans.add(beanInitialized);
    }

}
