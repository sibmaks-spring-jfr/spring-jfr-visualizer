package io.github.sibmaks.spring.jfr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sibmaks.spring.jfr.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
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

    /**
     * Generate JavaScript beans report
     *
     * @param rootReport report data
     */
    public void generateReport(
            io.github.sibmaks.spring.jfr.dto.view.common.RootReport rootReport
    ) {
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
            var reportJson = objectMapper.writeValueAsString(rootReport);
            writer.write(String.format("window.rootReport = %s;%n", reportJson));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

    }
}
