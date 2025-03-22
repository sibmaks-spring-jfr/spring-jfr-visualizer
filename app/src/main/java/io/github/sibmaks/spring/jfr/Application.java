package io.github.sibmaks.spring.jfr;

import io.github.sibmaks.spring.jfr.service.ReportService;
import io.github.sibmaks.spring.jfr.service.RootReportReader;
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

            var reader = context.getBean(RootReportReader.class);

            var rootReport = reader.read(path);

            var reportService = context.getBean(ReportService.class);
            reportService.generateReport(rootReport);
        }
    }
}
