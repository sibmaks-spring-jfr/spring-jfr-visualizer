package io.github.sibmaks.spring.jfr.service;

import io.github.sibmaks.spring.jfr.bus.InMemoryEventBus;
import io.github.sibmaks.spring.jfr.dto.protobuf.processing.Event;
import io.github.sibmaks.spring.jfr.dto.view.common.CommonDto;
import io.github.sibmaks.spring.jfr.dto.view.common.RootReport;
import io.github.sibmaks.spring.jfr.report.BeansReportCreator;
import io.github.sibmaks.spring.jfr.report.calls.CallsReportCreator;
import io.github.sibmaks.spring.jfr.report.connections.ConnectionsReportCreator;
import io.github.sibmaks.spring.jfr.report.kafka.consumer.KafkaConsumersReportCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RootReportReader {
    private final BeansReportCreator beansReportCreator;
    private final CallsReportCreator callsReportCreator;
    private final ConnectionsReportCreator connectionsReportCreator;
    private final KafkaConsumersReportCreator kafkaConsumersReportCreator;
    private final StringConstantRegistry stringConstantRegistry;
    private final InMemoryEventBus bus;

    public RootReport read(Path path) {
        try (var in = Files.newInputStream(path)) {
            Event event;
            while ((event = Event.parseDelimitedFrom(in)) != null) {
                try {
                    var eventType = Class.forName(event.getEventName());
                    bus.publish(eventType, event);
                } catch (ClassNotFoundException ignored) {
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var beansReport = beansReportCreator.create();
        var callsReport = callsReportCreator.create();
        var connectionsReport = connectionsReportCreator.get();
        var kafkaConsumersReport = kafkaConsumersReportCreator.get();

        var common = CommonDto.builder()
                .stringConstants(stringConstantRegistry.getConstants())
                .build();

        return RootReport.builder()
                .common(common)
                .beans(beansReport)
                .calls(callsReport)
                .connections(connectionsReport)
                .kafkaConsumers(kafkaConsumersReport)
                .build();
    }

}
