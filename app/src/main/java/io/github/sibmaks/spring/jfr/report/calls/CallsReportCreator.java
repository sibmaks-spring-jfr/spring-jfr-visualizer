package io.github.sibmaks.spring.jfr.report.calls;

import io.github.sibmaks.spring.jfr.dto.view.calls.CallTrace;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallsReport;
import io.github.sibmaks.spring.jfr.dto.view.calls.InvocationType;
import io.github.sibmaks.spring.jfr.event.reading.api.common.InvocationExecutedRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.common.InvocationFailedRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.component.ComponentMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.controller.ControllerMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.jpa.JPAMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.scheduled.ScheduledMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.service.ServiceMethodCalledRecordedEvent;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.sibmaks.spring.jfr.utils.JavaFlightRecorderUtils.getThreadName;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CallsReportCreator {
    private final StringConstantRegistry stringConstantRegistry;
    private final Map<Long, List<CallTrace.CallTraceBuilder>> contextToParentCalls = new HashMap<>();
    private final Map<Long, CallTrace.CallTraceBuilder> allCalls = new HashMap<>();
    private final Map<Long, List<Long>> children = new HashMap<>();

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

    @EventListener
    public void onControllerMethodCalled(ControllerMethodCalledRecordedEvent event) {
        var invocationId = event.getInvocationId();
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);

        var details = new LinkedHashMap<Long, Long>();
        addDetail(details, "HTTP Method", event.getHttpMethod());
        addDetail(details, "HTTP URL", event.getUrl());
        addDetail(details, "Rest", String.valueOf(event.isRest()));

        var contextId = stringConstantRegistry.getOrRegister(event.getContextId());
        var trace = CallTrace.builder()
                .contextId(contextId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.CONTROLLER.name()))
                .startTime(event.getStartTime().toEpochMilli())
                .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                .details(details)
                .children(new ArrayList<>());

        var contextParentCalls = contextToParentCalls.computeIfAbsent(contextId, it -> new ArrayList<>());
        contextParentCalls.add(trace);

        allCalls.put(invocationIdCode, trace);
    }

    @EventListener
    public void onScheduledMethodCalled(ScheduledMethodCalledRecordedEvent event) {
        var invocationId = event.getInvocationId();
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var contextId = stringConstantRegistry.getOrRegister(event.getContextId());
        var trace = CallTrace.builder()
                .contextId(contextId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.SCHEDULED.name()))
                .startTime(event.getStartTime().toEpochMilli())
                .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                .children(new ArrayList<>());

        var contextParentCalls = contextToParentCalls.computeIfAbsent(contextId, it -> new ArrayList<>());
        contextParentCalls.add(trace);

        allCalls.put(invocationIdCode, trace);
    }

    @EventListener
    public void onJPAMethodCalled(JPAMethodCalledRecordedEvent event) {
        var correlationId = stringConstantRegistry.getOrRegister(event.getCorrelationId());
        var invocationId = event.getInvocationId();
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var contextId = stringConstantRegistry.getOrRegister(event.getContextId());
        var trace = CallTrace.builder()
                .contextId(contextId)
                .correlationId(correlationId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.JPA.name()))
                .startTime(event.getStartTime().toEpochMilli())
                .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                .children(new ArrayList<>());

        saveChildTrace(invocationIdCode, trace, correlationId, contextId);
    }

    @EventListener
    public void onServiceMethodCalled(ServiceMethodCalledRecordedEvent event) {
        var correlationId = stringConstantRegistry.getOrRegister(event.getCorrelationId());
        var invocationId = event.getInvocationId();
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var contextId = stringConstantRegistry.getOrRegister(event.getContextId());
        var trace = CallTrace.builder()
                .contextId(contextId)
                .correlationId(correlationId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.SERVICE.name()))
                .startTime(event.getStartTime().toEpochMilli())
                .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                .children(new ArrayList<>());

        saveChildTrace(invocationIdCode, trace, correlationId, contextId);
    }

    @EventListener
    public void onComponentMethodCalled(ComponentMethodCalledRecordedEvent event) {
        var correlationId = stringConstantRegistry.getOrRegister(event.getCorrelationId());
        var invocationId = event.getInvocationId();
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var contextId = stringConstantRegistry.getOrRegister(event.getContextId());
        var trace = CallTrace.builder()
                .contextId(contextId)
                .correlationId(correlationId)
                .invocationId(invocationIdCode)
                .type(stringConstantRegistry.getOrRegister(InvocationType.COMPONENT.name()))
                .startTime(event.getStartTime().toEpochMilli())
                .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                .children(new ArrayList<>());

        saveChildTrace(invocationIdCode, trace, correlationId, contextId);
    }

    private void saveChildTrace(
            long invocationId,
            CallTrace.CallTraceBuilder childTrace,
            long correlationId,
            long contextId
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

    @EventListener
    public void onInvocationExecuted(InvocationExecutedRecordedEvent event) {
        var invocationId = event.getInvocationId();
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var callTrace = allCalls.get(invocationIdCode);
        if (callTrace == null) {
            log.warn("No call trace for invocation {}", invocationId);
            return;
        }

        var endTime = event.getEndTime();

        callTrace
                .success(1)
                .endTime(endTime.toEpochMilli());
    }

    @EventListener
    public void onInvocationFailed(InvocationFailedRecordedEvent event) {
        var invocationId = event.getInvocationId();
        var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);
        var callTrace = allCalls.get(invocationIdCode);
        if (callTrace == null) {
            log.warn("No call trace for fail invocation {}", invocationId);
            return;
        }

        var details = new LinkedHashMap<Long, Long>();
        addDetail(details, "Exception Class", event.getExceptionClass());
        addDetail(details, "Exception Message", event.getExceptionMessage());

        var endTime = event.getEndTime();

        callTrace
                .success(0)
                .endTime(endTime.toEpochMilli())
                .addDetails(details);
    }

    private void addDetail(LinkedHashMap<Long, Long> details, String title, String message) {
        details.put(stringConstantRegistry.getOrRegister(title), stringConstantRegistry.getOrRegister(message));
    }

}
