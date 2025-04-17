package io.github.sibmaks.spring.jfr;

import io.github.sibmaks.spring.jfr.service.ReportService;
import io.github.sibmaks.spring.jfr.service.RootReportReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            log.error("Usage: visualizer.jar <recording-file-path>");
            return;
        }
        var path = Path.of(args[0]);

        var sortedPath = Files.createTempFile("jfr-sorted-", ".proto");
        try (var context = SpringApplication.run(Application.class);) {
            ExternalJfrSorter.sort(path, sortedPath, 100_000);

            var reader = context.getBean(RootReportReader.class);

            var rootReport = reader.read(sortedPath);

            var reportService = context.getBean(ReportService.class);
            reportService.generateReport(rootReport);
        } finally {
            Files.deleteIfExists(sortedPath);
        }
    }

}
