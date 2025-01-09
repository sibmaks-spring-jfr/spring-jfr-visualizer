package io.github.sibmaks.spring.jfr.dto.recorded;


import io.github.sibmaks.spring.jfr.dto.recorded.bean.BeanDefinitionRegisteredRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.bean.PostProcessAfterInitializationRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.bean.PostProcessBeforeInitializationRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.common.InvocationExecutedRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.common.InvocationFailedRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.component.ComponentMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.controller.ControllerMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.jpa.JPAMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.scheduled.ScheduledMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.service.ServiceMethodCalledRecordedEvent;
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
    private final Queue<ServiceMethodCalledRecordedEvent> serviceMethodCalledRecordedEvents;
    private final Queue<ComponentMethodCalledRecordedEvent> componentMethodCalledRecordedEvents;
    private final Queue<ScheduledMethodCalledRecordedEvent> scheduledMethodCalledRecordedEvents;
    private final Map<String, Instant> executedInvocations;
    private final Map<String, InvocationFailedRecordedEvent> failedInvocations;
    private int count;

    public RecordedEvents() {
        this.beanDefinitionRegistered = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.beforeBeanInitializations = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.afterBeanInitializations = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.controllerMethodCalledRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.jpaMethodCalledRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.scheduledMethodCalledRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.serviceMethodCalledRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.componentMethodCalledRecordedEvents = new PriorityQueue<>(Comparator.comparing(RecordedData::getStartTime));
        this.executedInvocations = new HashMap<>();
        this.failedInvocations = new HashMap<>();
    }

    public void add(RecordedData event) {
        if (event instanceof BeanDefinitionRegisteredRecordedEvent recordedEvent) {
            beanDefinitionRegistered.add(recordedEvent);
        } else if (event instanceof PostProcessBeforeInitializationRecordedEvent recordedEvent) {
            beforeBeanInitializations.add(recordedEvent);
        } else if (event instanceof PostProcessAfterInitializationRecordedEvent recordedEvent) {
            afterBeanInitializations.add(recordedEvent);
        } else if (event instanceof ControllerMethodCalledRecordedEvent recordedEvent) {
            controllerMethodCalledRecordedEvents.add(recordedEvent);
        } else if (event instanceof JPAMethodCalledRecordedEvent recordedEvent) {
            jpaMethodCalledRecordedEvents.add(recordedEvent);
        } else if (event instanceof ScheduledMethodCalledRecordedEvent recordedEvent) {
            scheduledMethodCalledRecordedEvents.add(recordedEvent);
        }  else if (event instanceof ServiceMethodCalledRecordedEvent recordedEvent) {
            serviceMethodCalledRecordedEvents.add(recordedEvent);
        }  else if (event instanceof ComponentMethodCalledRecordedEvent recordedEvent) {
            componentMethodCalledRecordedEvents.add(recordedEvent);
        } else if (event instanceof InvocationExecutedRecordedEvent recordedEvent) {
            var invocationId = recordedEvent.getInvocationId();
            executedInvocations.put(invocationId, recordedEvent.getStartTime());
        } else if (event instanceof InvocationFailedRecordedEvent recordedEvent) {
            failedInvocations.put(recordedEvent.getInvocationId(), recordedEvent);
        } else {
            throw new IllegalArgumentException("Unknown event type: " + event.getClass().getName());
        }
        count++;
    }
}
