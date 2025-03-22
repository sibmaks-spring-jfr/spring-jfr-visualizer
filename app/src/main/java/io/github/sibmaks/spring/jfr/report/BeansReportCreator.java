package io.github.sibmaks.spring.jfr.report;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedEvents;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeanDefinition;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeanInitialized;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeansReport;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public BeansReport create(RecordedEvents events) {
        var beanDefinitions = getBeanDefinitions(events);

        var beans = getBeans(events);

        return BeansReport.builder()
                .beanDefinitions(beanDefinitions)
                .beans(beans)
                .build();
    }

    private ArrayList<BeanInitialized> getBeans(RecordedEvents events) {
        var preConstructed = new HashMap<String, Map<String, Instant>>();
        for (var event : events.getBeforeBeanInitializations()) {
            var contextId = event.getContextId();
            var beanName = event.getBeanName();
            var startTime = event.getStartTime();
            preConstructed.computeIfAbsent(contextId, k -> new HashMap<>())
                    .put(beanName, startTime);
        }

        var beans = new ArrayList<BeanInitialized>();
        for (var event : events.getAfterBeanInitializations()) {
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
                beans.add(beanInitialized);
            } else {
                var between = Duration.between(startTime, endTime);
                var duration = between.toNanos() / 1_000_000.;
                var beanInitialized = BeanInitialized.builder()
                        .contextId(stringConstantRegistry.getOrRegister(contextId))
                        .beanName(stringConstantRegistry.getOrRegister(beanName))
                        .duration(duration)
                        .preInitializedAt(startTime.toEpochMilli())
                        .postInitializedAt(endTime.toEpochMilli())
                        .build();
                beans.add(beanInitialized);
            }
        }
        return beans;
    }

    private Map<Long, List<BeanDefinition>> getBeanDefinitions(RecordedEvents events) {
        var beanDefinitions = new HashMap<Long, Map<String, BeanDefinition>>();

        for (var event : events.getBeanDefinitionRegistered()) {
            var beanDefinition = new BeanDefinition(stringConstantRegistry, event);
            var contextStringId = stringConstantRegistry.getOrRegister(event.getContextId());
            var contextBeanDefinitions = beanDefinitions.computeIfAbsent(contextStringId, k -> new HashMap<>());
            contextBeanDefinitions.put(event.getBeanName(), beanDefinition);
        }

        for (var event : events.getMergedBeanDefinitionRegistered()) {
            var contextStringId = stringConstantRegistry.getOrRegister(event.getContextId());
            var contextBeanDefinitions = beanDefinitions.computeIfAbsent(contextStringId, k -> new HashMap<>());
            var beanName = event.getBeanName();
            var existedBeanDefinition = contextBeanDefinitions.get(beanName);
            if (existedBeanDefinition != null) {
                existedBeanDefinition.patch(stringConstantRegistry, event);
            } else {
                var beanDefinition = new BeanDefinition(stringConstantRegistry, event);
                contextBeanDefinitions.put(beanName, beanDefinition);
            }
        }

        for (var event : events.getResolveBeanDependencyRecordedEvents()) {
            var contextStringId = stringConstantRegistry.getOrRegister(event.getContextId());
            var contextBeanDefinitions = beanDefinitions.get(contextStringId);
            if (contextBeanDefinitions == null) {
                continue;
            }
            var beanName = event.getDependentBeanName();
            var existedBeanDefinition = contextBeanDefinitions.get(beanName);
            if (existedBeanDefinition != null) {
                var dependencyBeanName = event.getDependencyBeanName();
                existedBeanDefinition.patch(stringConstantRegistry.getOrRegister(dependencyBeanName));
            }
        }

        return beanDefinitions.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                it -> new ArrayList<>(it.getValue().values())
                        )
                );
    }

}
