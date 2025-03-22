package io.github.sibmaks.spring.jfr;

import io.github.sibmaks.spring.jfr.dto.view.common.CommonDto;
import io.github.sibmaks.spring.jfr.dto.view.common.RootReport;
import io.github.sibmaks.spring.jfr.report.BeansReportCreator;
import io.github.sibmaks.spring.jfr.report.calls.CallsReportCreator;
import io.github.sibmaks.spring.jfr.report.connections.ConnectionsReportCreator;
import io.github.sibmaks.spring.jfr.service.EventReader;
import io.github.sibmaks.spring.jfr.service.ReportService;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.nio.file.Path;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@ComponentScan(basePackageClasses = Application.class)
public class Application {

    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Usage: visualizer.jar <recording-file-path>");
            return;
        }
        var path = Path.of(args[0]);

        try (var context = new AnnotationConfigApplicationContext(Application.class)) {

            var eventReader = context.getBean(EventReader.class);

            var events = eventReader.read(path);

            var beansReportCreator = context.getBean(BeansReportCreator.class);
            var beansReport = beansReportCreator.create(events);

            var callsReportCreator = context.getBean(CallsReportCreator.class);
            var callsReport = callsReportCreator.create();

            var connectionsReportCreator = context.getBean(ConnectionsReportCreator.class);
            var connectionsReport = connectionsReportCreator.get();

            var stringConstantRegistry = context.getBean(StringConstantRegistry.class);

            var common = CommonDto.builder()
                    .stringConstants(stringConstantRegistry.getConstants())
                    .build();

            var rootReport = RootReport.builder()
                    .common(common)
                    .beans(beansReport)
                    .calls(callsReport)
                    .connections(connectionsReport)
                    .build();

            var reportService = context.getBean(ReportService.class);
            reportService.generateReport(rootReport);
        }
    }
}
