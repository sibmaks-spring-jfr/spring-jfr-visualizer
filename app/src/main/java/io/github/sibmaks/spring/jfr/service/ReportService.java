package io.github.sibmaks.spring.jfr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sibmaks.spring.jfr.Application;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeansReport;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallsReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Service
public class ReportService {
    private final ObjectMapper objectMapper;
    private final String reportFilePath;

    public ReportService(ObjectMapper objectMapper,
                         @Value("${app.report.file}") String reportFilePath) {
        this.objectMapper = objectMapper;
        this.reportFilePath = reportFilePath;
    }

    /**
     * Generate JavaScript beans report
     *
     * @param beansReport beans report data
     * @param callsReport calls report data
     */
    public void generateReport(BeansReport beansReport, CallsReport callsReport) {
        var reportFile = new File(reportFilePath);
        var parentFile = reportFile.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                throw new RuntimeException("Couldn't create directory " + parentFile);
            }
        } else if (!parentFile.isDirectory()) {
            throw new RuntimeException("Not a directory: " + parentFile);
        }

        log.info("Creating bean report...");
        try {
            copyStaticFiles(parentFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var fileWriter = new FileWriter(reportFile);
             var writer = new BufferedWriter(fileWriter)) {
            var beansJson = objectMapper.writeValueAsString(beansReport);
            var callsJson = objectMapper.writeValueAsString(callsReport);
            var beansJsonVariable = objectMapper.writeValueAsString(beansJson);
            var callsJsonVariable = objectMapper.writeValueAsString(callsJson);
            writer.write(String.format("window.beansJson = %s;\nwindow.callsJson = %s;", beansJsonVariable, callsJsonVariable));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void copyStaticFiles(File destinationFolder) throws IOException {
        var classLoader = Application.class.getClassLoader();

        var staticFolder = new File(Objects.requireNonNull(classLoader.getResource("static")).getFile());

        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        copyFolder(staticFolder, destinationFolder);
    }

    private static void copyFolder(File source, File destination) throws IOException {
        if (!source.isDirectory()) {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        if (!destination.exists()) {
            destination.mkdirs();
        }

        var files = source.list();

        if (files == null) {
            return;
        }
        for (var file : files) {
            var srcFile = new File(source, file);
            var destFile = new File(destination, file);
            copyFolder(srcFile, destFile);
        }
    }

}
