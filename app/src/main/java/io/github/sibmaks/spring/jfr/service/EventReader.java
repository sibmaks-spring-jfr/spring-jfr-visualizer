package io.github.sibmaks.spring.jfr.service;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.dto.recorded.RecordedEvents;
import io.github.sibmaks.spring.jfr.dto.recorded.bean.BeanDefinitionRegisteredRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.bean.PostProcessAfterInitializationRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.bean.PostProcessBeforeInitializationRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.common.InvocationExecutedRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.common.InvocationFailedRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.controller.ControllerMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.jpa.JPAMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.recorded.scheduled.ScheduledMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.event.core.RecordedEventProxyFactory;
import io.github.sibmaks.spring.jfr.event.publish.async.AsyncMethodExecutedEvent;
import io.github.sibmaks.spring.jfr.event.publish.async.AsyncMethodFailedEvent;
import io.github.sibmaks.spring.jfr.event.publish.bean.BeanDefinitionRegisteredEvent;
import io.github.sibmaks.spring.jfr.event.publish.bean.PostProcessAfterInitializationEvent;
import io.github.sibmaks.spring.jfr.event.publish.bean.PostProcessBeforeInitializationEvent;
import io.github.sibmaks.spring.jfr.event.publish.controller.ControllerMethodCalledEvent;
import io.github.sibmaks.spring.jfr.event.publish.controller.ControllerMethodExecutedEvent;
import io.github.sibmaks.spring.jfr.event.publish.controller.ControllerMethodFailedEvent;
import io.github.sibmaks.spring.jfr.event.publish.jpa.JPAMethodCalledEvent;
import io.github.sibmaks.spring.jfr.event.publish.jpa.JPAMethodExecutedEvent;
import io.github.sibmaks.spring.jfr.event.publish.jpa.JPAMethodFailedEvent;
import io.github.sibmaks.spring.jfr.event.publish.scheduled.ScheduledMethodCalledEvent;
import io.github.sibmaks.spring.jfr.event.publish.scheduled.ScheduledMethodExecutedEvent;
import io.github.sibmaks.spring.jfr.event.publish.scheduled.ScheduledMethodFailedEvent;
import jdk.jfr.consumer.RecordingFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Service
public class EventReader {
    private final RecordedEventProxyFactory recordedEventProxyFactory;
    private final Map<String, Class<? extends RecordedData>> eventTypeName2Type;

    public EventReader(RecordedEventProxyFactory recordedEventProxyFactory) {
        this.recordedEventProxyFactory = recordedEventProxyFactory;
        this.eventTypeName2Type = Map.ofEntries(
                Map.entry(
                        BeanDefinitionRegisteredEvent.class.getCanonicalName(),
                        BeanDefinitionRegisteredRecordedEvent.class
                ),
                Map.entry(
                        PostProcessBeforeInitializationEvent.class.getCanonicalName(),
                        PostProcessBeforeInitializationRecordedEvent.class
                ),
                Map.entry(
                        PostProcessAfterInitializationEvent.class.getCanonicalName(),
                        PostProcessAfterInitializationRecordedEvent.class
                ),
                Map.entry(
                        ControllerMethodCalledEvent.class.getCanonicalName(),
                        ControllerMethodCalledRecordedEvent.class
                ),
                Map.entry(
                        JPAMethodCalledEvent.class.getCanonicalName(),
                        JPAMethodCalledRecordedEvent.class
                ),
                Map.entry(
                        ScheduledMethodCalledEvent.class.getCanonicalName(),
                        ScheduledMethodCalledRecordedEvent.class
                ),
                Map.entry(
                        AsyncMethodExecutedEvent.class.getCanonicalName(),
                        InvocationExecutedRecordedEvent.class
                ),
                Map.entry(
                        ControllerMethodExecutedEvent.class.getCanonicalName(),
                        InvocationExecutedRecordedEvent.class
                ),
                Map.entry(
                        JPAMethodExecutedEvent.class.getCanonicalName(),
                        InvocationExecutedRecordedEvent.class
                ),
                Map.entry(
                        ScheduledMethodExecutedEvent.class.getCanonicalName(),
                        InvocationExecutedRecordedEvent.class
                ),
                Map.entry(
                        AsyncMethodFailedEvent.class.getCanonicalName(),
                        InvocationFailedRecordedEvent.class
                ),
                Map.entry(
                        ControllerMethodFailedEvent.class.getCanonicalName(),
                        InvocationFailedRecordedEvent.class
                ),
                Map.entry(
                        JPAMethodFailedEvent.class.getCanonicalName(),
                        InvocationFailedRecordedEvent.class
                ),
                Map.entry(
                        ScheduledMethodFailedEvent.class.getCanonicalName(),
                        InvocationFailedRecordedEvent.class
                )
        );
    }

    public RecordedEvents read(Path path) {
        var recordedEvents = new RecordedEvents();
        try (var recordingFile = new RecordingFile(path)) {
            log.info("Reading events in {}", path);
            while (recordingFile.hasMoreEvents()) {
                var recordedEvent = recordingFile.readEvent();
                var eventType = recordedEvent.getEventType();
                var eventTypeName = eventType.getName();
                var eventClass = eventTypeName2Type.get(eventTypeName);
                if (eventClass == null) {
                    continue;
                }
                var event = recordedEventProxyFactory.create(recordedEvent, eventClass);
                recordedEvents.add(event);
            }
        } catch (IOException | RecordedEventProxyFactory.ConversionException e) {
            throw new RuntimeException(e);
        }
        log.info("{} events read successfully", recordedEvents.getCount());
        return recordedEvents;
    }

}
