package io.github.sibmaks.spring.jfr.service;

import io.github.sibmaks.spring.jfr.Application;
import io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList;
import io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized;
import io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeansReport;
import io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTraceList;
import io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallsReport;
import io.github.sibmaks.spring.jfr.dto.protobuf.common.CommonDto;
import io.github.sibmaks.spring.jfr.dto.protobuf.connections.*;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeanDefinition;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallTrace;
import io.github.sibmaks.spring.jfr.dto.view.common.RootReport;
import io.github.sibmaks.spring.jfr.dto.view.connections.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Service
public class ReportService {
    private final String reportFilePath;

    public ReportService(@Value("${app.report.file}") String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

    private static void copyStaticFiles(File destinationFolder) throws IOException {
        var classLoader = Application.class.getClassLoader();

        var staticFolder = new File(Objects.requireNonNull(classLoader.getResource("static")).getFile());

        if (!destinationFolder.exists()) {
            if (!destinationFolder.mkdirs()) {
                throw new IOException("Could not create folder " + destinationFolder.getAbsolutePath());
            }
        }

        copyFolder(staticFolder, destinationFolder);
    }

    private static void copyFolder(File source, File destination) throws IOException {
        if (!source.isDirectory()) {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return;
        }
        if (!destination.exists()) {
            if (!destination.mkdirs()) {
                throw new IOException("Could not create folder " + destination.getAbsolutePath());
            }
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

    private static byte[] toByteArray(io.github.sibmaks.spring.jfr.dto.protobuf.common.RootReport rootReport) {
        try (var byteOutputStream = new ByteArrayOutputStream()) {
            rootReport.writeTo(byteOutputStream);
            byteOutputStream.flush();
            return byteOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static io.github.sibmaks.spring.jfr.dto.protobuf.common.RootReport map(RootReport rootReport) {
        return io.github.sibmaks.spring.jfr.dto.protobuf.common.RootReport.newBuilder()
                .setCommon(map(rootReport.common()))
                .setBeans(map(rootReport.beans()))
                .setCalls(map(rootReport.calls()))
                .setConnections(map(rootReport.connections()))
                .build();
    }

    private static CommonDto map(io.github.sibmaks.spring.jfr.dto.view.common.CommonDto common) {
        return CommonDto.newBuilder()
                .addAllStringConstants(common.stringConstants())
                .build();
    }

    private static BeansReport map(io.github.sibmaks.spring.jfr.dto.view.beans.BeansReport beans) {
        return BeansReport.newBuilder()
                .putAllBeanDefinitions(map(beans.beanDefinitions()))
                .addAllBeans(mapBeanInitialized(beans.beans()))
                .build();
    }

    private static Map<Integer, BeanDefinitionList> map(Map<Integer, List<BeanDefinition>> definitions) {
        if (definitions == null) {
            return Collections.emptyMap();
        }
        return definitions.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, it -> mapDefinitionList(it.getValue())));
    }

    private static BeanDefinitionList mapDefinitionList(List<BeanDefinition> value) {
        return BeanDefinitionList.newBuilder()
                .addAllBeanDefinitions(
                        Optional.ofNullable(value)
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(ReportService::map)
                                .toList()
                )
                .build();
    }

    private static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinition map(BeanDefinition it) {
        return io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinition.newBuilder()
                .setScope(it.getScope())
                .setClassName(it.getClassName())
                .setName(it.getName())
                .setPrimary(it.getPrimary())
                .addAllDependencies(
                        Optional.ofNullable(it.getDependencies())
                                .orElseGet(Collections::emptySortedSet)
                                .stream()
                                .toList()
                )
                .setStereotype(it.getStereotype())
                .setGenerated(it.isGenerated())
                .build();
    }

    private static Iterable<BeanInitialized> mapBeanInitialized(List<io.github.sibmaks.spring.jfr.dto.view.beans.BeanInitialized> beans) {
        return Optional.ofNullable(beans)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(ReportService::map)
                .toList();
    }

    private static BeanInitialized map(io.github.sibmaks.spring.jfr.dto.view.beans.BeanInitialized it) {
        var builder = BeanInitialized.newBuilder()
                .setContextId(it.getContextId())
                .setBeanName(it.getBeanName())
                .setDuration(it.getDuration());

        var preInitializedAt = it.getPreInitializedAt();
        if(preInitializedAt != null) {
            builder.setPreInitializedAt(preInitializedAt);
        }

        var postInitializedAt = it.getPostInitializedAt();
        if(postInitializedAt != null) {
            builder.setPostInitializedAt(postInitializedAt);
        }

        return builder.build();
    }

    private static CallsReport map(io.github.sibmaks.spring.jfr.dto.view.calls.CallsReport calls) {
        return CallsReport.newBuilder()
                .putAllContexts(mapCallsContext(calls.contexts()))
                .build();
    }

    private static Map<Integer, CallTraceList> mapCallsContext(Map<Integer, List<CallTrace>> contexts) {
        if (contexts == null) {
            return Collections.emptyMap();
        }
        return contexts.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, it -> mapCallTraceList(it.getValue())));
    }

    private static CallTraceList mapCallTraceList(List<CallTrace> value) {
        return CallTraceList.newBuilder()
                .addAllCallTraces(
                        Optional.ofNullable(value)
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(ReportService::map)
                                .toList()
                )
                .build();
    }

    private static io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace map(CallTrace callTrace) {
        return io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace.newBuilder()
                .setInvocationId(callTrace.invocationId())
                .setSuccess(callTrace.success())
                .setType(callTrace.type())
                .setStartTime(callTrace.startTime())
                .setEndTime(callTrace.endTime())
                .setThreadName(callTrace.threadName())
                .setClassName(callTrace.className())
                .setMethodName(callTrace.methodName())
                .putAllDetails(
                        Optional.ofNullable(callTrace.details())
                                .orElseGet(Collections::emptyMap)
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                )
                .addAllChildren(
                        Optional.ofNullable(callTrace.children())
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(ReportService::map)
                                .toList()
                )
                .build();
    }

    private static ConnectionsReport map(io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionsReport connections) {
        return ConnectionsReport.newBuilder()
                .putAllContexts(
                        Optional.ofNullable(connections.contexts())
                                .orElseGet(Collections::emptyMap)
                                .entrySet()
                                .stream()
                                .collect(
                                        Collectors.toMap(
                                                Map.Entry::getKey,
                                                it -> mapContextConnections(it.getValue())
                                        )
                                )
                )
                .build();
    }

    private static ConnectionMap mapContextConnections(Map<Integer, List<Connection>> value) {
        return ConnectionMap.newBuilder()
                .putAllConnections(
                        Optional.ofNullable(value)
                                .orElseGet(Collections::emptyMap)
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        it -> mapConnectionList(it.getValue())
                                ))
                )
                .build();
    }

    private static ConnectionList mapConnectionList(List<Connection> value) {
        return ConnectionList.newBuilder()
                .addAllConnections(
                        Optional.ofNullable(value)
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .map(ReportService::map)
                                .toList()
                )
                .build();
    }

    private static io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection map(Connection it) {
        return io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.newBuilder()
                .addAllEvents(mapConnectionEvents(it.getEvents()))
                .setId(it.getId())
                .setDuration(it.getDuration())
                .setHasExceptions(it.isHasExceptions())
                .build();
    }

    private static Iterable<ConnectionEvent> mapConnectionEvents(List<io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionEvent> events) {
        return Optional.ofNullable(events)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(ReportService::map)
                .toList();
    }

    private static ConnectionEvent map(io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionEvent it) {
        var builder = ConnectionEvent.newBuilder()
                .setIndex(it.index())
                .setAction(it.action())
                .setStartedAt(it.startedAt())
                .setFinishedAt(it.finishedAt())
                .setThreadName(it.threadName());

        var transactionIsolation = it.transactionIsolation();
        if (transactionIsolation != null) {
            builder.setTransactionIsolation(transactionIsolation);
        }

        var exception = it.exception();
        if (exception != null) {
            builder.setException(map(exception));
        }

        return builder.build();
    }

    private static ConnectionException map(io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionException exception) {
        return ConnectionException.newBuilder()
                .setType(exception.type())
                .setMessage(exception.message())
                .build();
    }

    /**
     * Generate JavaScript beans report
     *
     * @param rootReport report data
     */
    public void generateReport(RootReport rootReport) {
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

        var protobufReport = map(rootReport);
        var serialized = toByteArray(protobufReport);

        try (var fileWriter = new FileWriter(reportFile);
             var writer = new BufferedWriter(fileWriter)) {
            var encoder = Base64.getEncoder();
            var r = encoder.encodeToString(serialized);
            writer.write(String.format("window.rootReport = '%s';", r));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        try (var fileOutputStream = new FileOutputStream(reportFile + ".bin");
             var writer = new BufferedOutputStream(fileOutputStream)) {
            writer.write(serialized);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
