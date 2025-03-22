package io.github.sibmaks.spring.jfr.service;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedEvents;
import io.github.sibmaks.spring.jfr.event.reading.core.recorded.RecordedEventFactory;
import jdk.jfr.consumer.RecordingFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventReader {
    private final RecordedEventFactory recordedEventFactory;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RecordedEvents read(Path path) {
        var recordedEvents = new RecordedEvents();
        try (var recordingFile = new RecordingFile(path)) {
            log.info("Reading events in {}", path);
            while (recordingFile.hasMoreEvents()) {
                var recordedEvent = recordingFile.readEvent();
                var event = recordedEventFactory.convert(recordedEvent);
                if (event == null) {
                    continue;
                }
                recordedEvents.add(event);
                applicationEventPublisher.publishEvent(event);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("{} events read successfully", recordedEvents.getCount());
        return recordedEvents;
    }

}
