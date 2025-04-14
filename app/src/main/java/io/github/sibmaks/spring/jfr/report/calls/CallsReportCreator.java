package io.github.sibmaks.spring.jfr.report.calls;

import io.github.sibmaks.spring.jfr.bus.SubscribeTo;
import io.github.sibmaks.spring.jfr.dto.protobuf.processing.Event;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallTrace;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallsReport;
import io.github.sibmaks.spring.jfr.dto.view.calls.InvocationType;
import io.github.sibmaks.spring.jfr.event.api.common.InvocationExecutedFact;
import io.github.sibmaks.spring.jfr.event.api.common.InvocationFailedFact;
import io.github.sibmaks.spring.jfr.event.recording.component.ComponentMethodCalledEvent;
import io.github.sibmaks.spring.jfr.event.recording.controller.ControllerMethodCalledEvent;
import io.github.sibmaks.spring.jfr.event.recording.jpa.JPAMethodCalledEvent;
import io.github.sibmaks.spring.jfr.event.recording.scheduled.ScheduledMethodCalledEvent;
import io.github.sibmaks.spring.jfr.event.recording.service.ServiceMethodCalledEvent;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CallsReportCreator {
    private final StringConstantRegistry stringConstantRegistry;
    private final Map<Integer, List<CallTrace.CallTraceBuilder>> contextToParentCalls = new HashMap<>();
    private final Map<Integer, CallTrace.CallTraceBuilder> allCalls = new HashMap<>();
    private final Map<Integer, List<Integer>> children = new HashMap<>();

    public CallsReport create() {
        var contextsTrace = contextToParentCalls.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, it -> map(it.getValue())));

        return CallsReport.builder()
                .contexts(contextsTrace)
                .build();
    }

    private List<CallTrace> map(List<CallTrace.CallTraceBuilder> value) {
        return value.stream()
                .map(CallTrace.CallTraceBuilder::build)
                .toList();
    }

    @SubscribeTo(ControllerMethodCalledEvent.class)
    public void onControllerMethodCalled(Event event) {
        var invocationId = event.getStringFieldsOrThrow("invocationId");
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);

        var details = new LinkedHashMap<Integer, Integer>();
        addDetail(details, "HTTP Method", event.getStringFieldsOrThrow("httpMethod"));
        addDetail(details, "HTTP URL", event.getStringFieldsOrThrow("url"));
        addDetail(details, "Rest", String.valueOf(event.getBoolFieldsOrThrow("rest")));

        var contextId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));
        var trace = CallTrace.builder()
                .contextId(contextId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.CONTROLLER.name()))
                .startTime(event.getStartTime())
                .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                .className(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("className")))
                .methodName(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("methodName")))
                .details(details)
                .children(new ArrayList<>());

        var contextParentCalls = contextToParentCalls.computeIfAbsent(contextId, it -> new ArrayList<>());
        contextParentCalls.add(trace);

        allCalls.put(invocationIdCode, trace);
    }

    @SubscribeTo(ScheduledMethodCalledEvent.class)
    public void onScheduledMethodCalled(Event event) {
        var invocationId = event.getStringFieldsOrThrow("invocationId");
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var contextId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));
        var trace = CallTrace.builder()
                .contextId(contextId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.SCHEDULED.name()))
                .startTime(event.getStartTime())
                .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                .className(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("className")))
                .methodName(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("methodName")))
                .children(new ArrayList<>());

        var contextParentCalls = contextToParentCalls.computeIfAbsent(contextId, it -> new ArrayList<>());
        contextParentCalls.add(trace);

        allCalls.put(invocationIdCode, trace);
    }

    @SubscribeTo(JPAMethodCalledEvent.class)
    public void onJPAMethodCalled(Event event) {
        var correlationId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrDefault("correlationId", null));
        var invocationId = event.getStringFieldsOrThrow("invocationId");
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var contextId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));
        var trace = CallTrace.builder()
                .contextId(contextId)
                .correlationId(correlationId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.JPA.name()))
                .startTime(event.getStartTime())
                .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                .className(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("className")))
                .methodName(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("methodName")))
                .children(new ArrayList<>());

        saveChildTrace(invocationIdCode, trace, correlationId, contextId);
    }

    @SubscribeTo(ServiceMethodCalledEvent.class)
    public void onServiceMethodCalled(Event event) {
        var correlationId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrDefault("correlationId", null));
        var invocationId = event.getStringFieldsOrThrow("invocationId");
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var contextId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));
        var trace = CallTrace.builder()
                .contextId(contextId)
                .correlationId(correlationId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.SERVICE.name()))
                .startTime(event.getStartTime())
                .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                .className(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("className")))
                .methodName(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("methodName")))
                .children(new ArrayList<>());

        saveChildTrace(invocationIdCode, trace, correlationId, contextId);
    }

    @SubscribeTo(ComponentMethodCalledEvent.class)
    public void onComponentMethodCalled(Event event) {
        var correlationId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrDefault("correlationId", null));
        var invocationId = event.getStringFieldsOrThrow("invocationId");
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var contextId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));
        var trace = CallTrace.builder()
                .contextId(contextId)
                .correlationId(correlationId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.COMPONENT.name()))
                .startTime(event.getStartTime())
                .threadName(stringConstantRegistry.getOrRegister(event.getJavaThreadName()))
                .className(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("className")))
                .methodName(stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("methodName")))
                .children(new ArrayList<>());

        saveChildTrace(invocationIdCode, trace, correlationId, contextId);
    }

    private void saveChildTrace(
            int invocationId,
            CallTrace.CallTraceBuilder childTrace,
            int correlationId,
            int contextId
    ) {
        allCalls.put(invocationId, childTrace);

        if (correlationId < 0) {
            var contextParentCalls = contextToParentCalls.computeIfAbsent(contextId, it -> new ArrayList<>());
            contextParentCalls.add(childTrace);
            return;
        }

        var parent = allCalls.get(correlationId);
        if (parent == null) {
            var contextParentCalls = contextToParentCalls.computeIfAbsent(contextId, it -> new ArrayList<>());
            contextParentCalls.add(childTrace);
            return;
        }

        parent.child(childTrace);

        children.computeIfAbsent(correlationId, it -> new ArrayList<>())
                .add(invocationId);
    }

    @SubscribeTo(InvocationExecutedFact.class)
    public void onInvocationExecuted(Event event) {
        var invocationId = event.getStringFieldsOrThrow("invocationId");
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var callTrace = allCalls.get(invocationIdCode);
        if (callTrace == null) {
            log.warn("No call trace for invocation {}", invocationId);
            return;
        }

        var endTime = event.getEndTime();

        callTrace
                .success(true)
                .endTime(endTime);
    }

    @SubscribeTo(InvocationFailedFact.class)
    public void onInvocationFailed(Event event) {
        var invocationId = event.getStringFieldsOrThrow("invocationId");
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var callTrace = allCalls.get(invocationIdCode);
        if (callTrace == null) {
            log.warn("No call trace for fail invocation {}", invocationId);
            return;
        }

        var details = new LinkedHashMap<Integer, Integer>();
        addDetail(details, "Exception Class", event.getStringFieldsOrThrow("exceptionClass"));
        addDetail(details, "Exception Message", event.getStringFieldsOrThrow("exceptionMessage"));

        var endTime = event.getEndTime();

        callTrace
                .success(false)
                .endTime(endTime)
                .addDetails(details);
    }

    private void addDetail(Map<Integer, Integer> details, String title, String message) {
        details.put(stringConstantRegistry.getOrRegister(title), stringConstantRegistry.getOrRegister(message));
    }

}
