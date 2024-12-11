package io.github.sibmaks.spring.jfr.dto.recorded;


import io.github.sibmaks.spring.jfr.dto.recorded.bean.BeanDefinitionRegisteredRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.bean.PostProcessAfterInitializationRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.bean.PostProcessBeforeInitializationRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.common.InvocationExecutedRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.common.InvocationFailedRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.controller.ControllerMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.jpa.JPAMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.scheduled.ScheduledMethodCalledRecordedEvent;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
public class RecordedEvents {
    private final Queue<BeanDefinitionRegisteredRecordedEvent> beanDefinitionRegistered;
    private final Queue<PostProcessBeforeInitializationRecordedEvent> beforeBeanInitializations;
    private final Queue<PostProcessAfterInitializationRecordedEvent> afterBeanInitializations;
    private final Queue<ControllerMethodCalledRecordedEvent> controllerMethodCalledRecordedEvents;
    private final Queue<JPAMethodCalledRecordedEvent> jpaMethodCalledRecordedEvents;
    private final Queue<ScheduledMethodCalledRecordedEvent> scheduledMethodCalledRecordedEvents;
    private final Map<String, Instant> executedInvocations;
    private final Map<String, Instant> failedInvocations;
    private int count;

    public RecordedEvents() {
        this.beanDefinitionRegistered = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.beforeBeanInitializations = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.afterBeanInitializations = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.controllerMethodCalledRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.jpaMethodCalledRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.scheduledMethodCalledRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.executedInvocations = new HashMap<>();
        this.failedInvocations = new HashMap<>();
    }

    public void add(RecordedData event) {
        if (event instanceof BeanDefinitionRegisteredRecordedEvent) {
            beanDefinitionRegistered.add((BeanDefinitionRegisteredRecordedEvent) event);
        } else if (event instanceof PostProcessBeforeInitializationRecordedEvent) {
            beforeBeanInitializations.add((PostProcessBeforeInitializationRecordedEvent) event);
        } else if (event instanceof PostProcessAfterInitializationRecordedEvent) {
            afterBeanInitializations.add((PostProcessAfterInitializationRecordedEvent) event);
        } else if (event instanceof ControllerMethodCalledRecordedEvent) {
            controllerMethodCalledRecordedEvents.add((ControllerMethodCalledRecordedEvent) event);
        } else if (event instanceof JPAMethodCalledRecordedEvent) {
            jpaMethodCalledRecordedEvents.add((JPAMethodCalledRecordedEvent) event);
        } else if (event instanceof ScheduledMethodCalledRecordedEvent) {
            scheduledMethodCalledRecordedEvents.add((ScheduledMethodCalledRecordedEvent) event);
        } else if (event instanceof InvocationExecutedRecordedEvent) {
            var invocationId = ((InvocationExecutedRecordedEvent) event).getInvocationId();
            executedInvocations.put(invocationId, event.getStartTime());
        } else if (event instanceof InvocationFailedRecordedEvent) {
            var invocationId = ((InvocationFailedRecordedEvent) event).getInvocationId();
            failedInvocations.put(invocationId, event.getStartTime());
        } else {
            throw new IllegalArgumentException("Unknown event type: " + event.getClass().getName());
        }
        count++;
    }
}
