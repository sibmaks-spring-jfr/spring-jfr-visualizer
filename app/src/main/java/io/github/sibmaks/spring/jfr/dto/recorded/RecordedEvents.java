package io.github.sibmaks.spring.jfr.dto.recorded;


import io.github.sibmaks.spring.jfr.event.reading.api.RecordedData;
import io.github.sibmaks.spring.jfr.event.reading.api.bean.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Getter
public class RecordedEvents {
    private final Queue<BeanDefinitionRegisteredRecordedEvent> beanDefinitionRegistered;
    private final Queue<MergedBeanDefinitionRegisteredRecordedEvent> mergedBeanDefinitionRegistered;
    private final Queue<ResolveBeanDependencyRecordedEvent> resolveBeanDependencyRecordedEvents;
    private final Queue<PostProcessBeforeInitializationRecordedEvent> beforeBeanInitializations;
    private final Queue<PostProcessAfterInitializationRecordedEvent> afterBeanInitializations;
    private int count;

    public RecordedEvents() {
        this.beanDefinitionRegistered = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.mergedBeanDefinitionRegistered = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.resolveBeanDependencyRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.beforeBeanInitializations = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.afterBeanInitializations = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
    }

    public void add(Object event) {
        if (event instanceof BeanDefinitionRegisteredRecordedEvent recordedEvent) {
            beanDefinitionRegistered.add(recordedEvent);
        } else if (event instanceof MergedBeanDefinitionRegisteredRecordedEvent recordedEvent) {
            mergedBeanDefinitionRegistered.add(recordedEvent);
        } else if (event instanceof ResolveBeanDependencyRecordedEvent recordedEvent) {
            resolveBeanDependencyRecordedEvents.add(recordedEvent);
        } else if (event instanceof PostProcessBeforeInitializationRecordedEvent recordedEvent) {
            beforeBeanInitializations.add(recordedEvent);
        } else if (event instanceof PostProcessAfterInitializationRecordedEvent recordedEvent) {
            afterBeanInitializations.add(recordedEvent);
        } else {
            //log.warn("Unknown event type: {}", event);
            return;
        }
        count++;
    }
}
