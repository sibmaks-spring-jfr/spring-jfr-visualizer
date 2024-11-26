package io.github.sibmaks.spring.jfr.report;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedEvents;
import io.github.sibmaks.spring.jfr.dto.view.BeanDefinition;
import io.github.sibmaks.spring.jfr.dto.view.BeanInitialized;
import io.github.sibmaks.spring.jfr.dto.view.BeansReport;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Component
public class BeansReportCreator {

    public BeansReport create(RecordedEvents events) {
        var beanDefinitions = getBeanDefinitions(events);

        var beans = getBeans(events);

        return BeansReport.builder()
                .beanDefinitions(beanDefinitions)
                .beans(beans)
                .build();
    }

    private static ArrayList<BeanInitialized> getBeans(RecordedEvents events) {
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
                        .contextId(contextId)
                        .beanName(beanName)
                        .duration(-1)
                        .postInitializedAt(endTime.toEpochMilli())
                        .build();
                beans.add(beanInitialized);
            } else {
                var between = Duration.between(startTime, endTime);
                var duration = between.toNanos() / 1000.;
                var beanInitialized = BeanInitialized.builder()
                        .contextId(contextId)
                        .beanName(beanName)
                        .duration(duration)
                        .preInitializedAt(startTime.toEpochMilli())
                        .postInitializedAt(endTime.toEpochMilli())
                        .build();
                beans.add(beanInitialized);
            }
        }
        return beans;
    }

    private static ArrayList<BeanDefinition> getBeanDefinitions(RecordedEvents events) {
        var beanDefinitions = new ArrayList<BeanDefinition>();

        for (var event : events.getBeanDefinitionRegistered()) {
            beanDefinitions.add(new BeanDefinition(event));
        }
        return beanDefinitions;
    }

}
