package io.github.sibmaks.spring.jfr.service;

import io.github.sibmaks.spring.jfr.dto.view.common.CommonDto;
import io.github.sibmaks.spring.jfr.dto.view.common.RootReport;
import io.github.sibmaks.spring.jfr.event.reading.core.recorded.RecordedEventFactory;
import io.github.sibmaks.spring.jfr.report.BeansReportCreator;
import io.github.sibmaks.spring.jfr.report.calls.CallsReportCreator;
import io.github.sibmaks.spring.jfr.report.connections.ConnectionsReportCreator;
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
public class RootReportReader {
    private final RecordedEventFactory recordedEventFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final BeansReportCreator beansReportCreator;
    private final CallsReportCreator callsReportCreator;
    private final ConnectionsReportCreator connectionsReportCreator;
    private final StringConstantRegistry stringConstantRegistry;

    public RootReport read(Path path) {
        try (var recordingFile = new RecordingFile(path)) {
            log.info("Reading events in {}", path);
            while (recordingFile.hasMoreEvents()) {
                var recordedEvent = recordingFile.readEvent();
                var event = recordedEventFactory.convert(recordedEvent);
                if (event == null) {
                    continue;
                }
                applicationEventPublisher.publishEvent(event);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var beansReport = beansReportCreator.create();
        var callsReport = callsReportCreator.create();
        var connectionsReport = connectionsReportCreator.get();

        var common = CommonDto.builder()
                .stringConstants(stringConstantRegistry.getConstants())
                .build();

        return RootReport.builder()
                .common(common)
                .beans(beansReport)
                .calls(callsReport)
                .connections(connectionsReport)
                .build();
    }

}
