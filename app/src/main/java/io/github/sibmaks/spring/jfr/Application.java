package io.github.sibmaks.spring.jfr;

import io.github.sibmaks.spring.jfr.event.bean.BeanDefinitionRegisteredEvent;
import io.github.sibmaks.spring.jfr.event.bean.PostProcessAfterInitializationEvent;
import io.github.sibmaks.spring.jfr.event.bean.PostProcessBeforeInitializationEvent;
import io.github.sibmaks.spring.jfr.event.converter.DependencyConverter;
import jdk.jfr.consumer.RecordingFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.1
 */
public class Application {
    private static final String EVENT_BEAN_NAME = "beanName";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: visualizer.jar <recording-file-path>");
            return;
        }
        var path = Path.of(args[0]);
        var beanDefinitions = new ArrayList<BeanDefinitionRegisteredEvent>();
        var beanStartTimes = new HashMap<String, Instant>();
        var beanInitialized = new ArrayList<BeanInitialized>();
        try (var recordingFile = new RecordingFile(path)) {
            System.out.println("Reading beanDefinitions one by one");
            System.out.println("=========================");
            while (recordingFile.hasMoreEvents()) {
                var recordedEvent = recordingFile.readEvent();
                var eventType = recordedEvent.getEventType();
                var eventTypeName = eventType.getName();
                if (!eventTypeName.startsWith("io.github.sibmaks.spring.jfr")) {
                    continue;
                }
                if (BeanDefinitionRegisteredEvent.class.getCanonicalName().equals(eventTypeName)) {
                    var beanName = recordedEvent.getString(EVENT_BEAN_NAME);
                    var event = BeanDefinitionRegisteredEvent.builder()
                            .scope(recordedEvent.getString("scope"))
                            .beanClassName(recordedEvent.getString("beanClassName"))
                            .beanName(beanName)
                            .primary(recordedEvent.getString("primary"))
                            .dependencies(DependencyConverter.convert(recordedEvent.getString("dependencies")))
                            .generated(recordedEvent.getBoolean("generated"))
                            .build();
                    beanDefinitions.add(event);
                    System.out.printf("Bean definition read: %s%n", beanName);
                    continue;
                }
                if (PostProcessBeforeInitializationEvent.class.getCanonicalName().equals(eventTypeName)) {
                    var beanName = recordedEvent.getString(EVENT_BEAN_NAME);
                    var startTime = recordedEvent.getStartTime();
                    beanStartTimes.put(beanName, startTime);
                    continue;
                }
                if (PostProcessAfterInitializationEvent.class.getCanonicalName().equals(eventTypeName)) {
                    var beanName = recordedEvent.getString(EVENT_BEAN_NAME);
                    var startTime = beanStartTimes.get(beanName);
                    var endTime = recordedEvent.getEndTime();
                    var duration = Duration.between(startTime, endTime);
                    beanInitialized.add(new BeanInitialized(beanName, duration.toNanos() / 1000.));
                }
            }
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        generateHtml(beanDefinitions, beanInitialized);
        System.out.println();
    }

    /**
     * Generate an HTML file using Thymeleaf.
     */
    public static void generateHtml(
            List<BeanDefinitionRegisteredEvent> beanDefinitions,
            List<BeanInitialized> beanInitialized
    ) {
        var templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");

        var templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        var context = new Context();
        context.setVariable("beanDefinitions", beanDefinitions);
        context.setVariable("beanInitialized", beanInitialized);

        var dependencies = beanDefinitions.stream()
                .collect(Collectors.toMap(BeanDefinitionRegisteredEvent::getBeanName, BeanDefinitionRegisteredEvent::getDependencies));
        context.setVariable("dependencies", dependencies);

        var htmlContent = templateEngine.process("beanEventsTemplate", context);

        var reportFile = new File("report", "BeanEvents.html");
        var parentFile = reportFile.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                throw new RuntimeException("Couldn't create directory " + parentFile);
            }
        } else if (!parentFile.isDirectory()) {
            throw new RuntimeException("Not a directory: " + parentFile);
        }

        System.out.println("Creating bean report...");
        try {
            copyStaticFiles(parentFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (var fileWriter = new FileWriter(reportFile);
             var writer = new BufferedWriter(fileWriter)) {
            writer.write(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Report created: file:///%s%n", reportFile.getAbsolutePath());
    }

    public static void copyStaticFiles(File destinationFolder) throws IOException {
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
