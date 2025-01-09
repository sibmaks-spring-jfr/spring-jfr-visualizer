package io.github.sibmaks.spring.jfr.report;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedEvents;
import io.github.sibmaks.spring.jfr.dto.recorded.common.InvocationFailedRecordedEvent;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallTrace;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallsReport;
import io.github.sibmaks.spring.jfr.dto.view.calls.InvocationType;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Component
public class CallsReportCreator {

    public CallsReport create(RecordedEvents events) {
        var roots = buildCallTraces(events);

        return CallsReport.builder()
                .roots(roots)
                .build();
    }

    private static List<CallTrace> buildCallTraces(RecordedEvents events) {
        var callTracesByStartId = new HashMap<String, CallTrace>();

        callTracesByStartId.putAll(readControllerEvents(events));
        callTracesByStartId.putAll(readJPAEvents(events));
        callTracesByStartId.putAll(readScheduledEvents(events));
        callTracesByStartId.putAll(readServiceEvents(events));
        callTracesByStartId.putAll(readComponentEvents(events));

        var roots = new ArrayList<CallTrace>();

        for (var trace : callTracesByStartId.values()) {
            var parentId = trace.getCorrelationId();
            if (parentId == null) {
                roots.add(trace);
            } else {
                var parent = callTracesByStartId.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(trace);
                } else {
                    roots.add(trace);
                }
            }
        }

        sortTracesRecursively(roots);

        return roots;
    }

    private static Map<String, CallTrace> readScheduledEvents(RecordedEvents events) {
        var scheduledEvents = events.getScheduledMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<String, CallTrace>();
        for (var event : scheduledEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }

            var parameters = getBasicParameters(failedFact);
            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(event.getContextId())
                    .correlationId(null)
                    .invocationId(invocationId)
                    .type(InvocationType.SCHEDULED)
                    .success(executedTimestamp != null)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .className(event.getClassName())
                    .methodName(event.getMethodName())
                    .parameters(parameters)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationId, trace);
        }
        return callTracesByStartId;
    }

    private static Map<String, CallTrace> readJPAEvents(RecordedEvents events) {
        var jpaEvents = events.getJpaMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<String, CallTrace>();
        for (var event : jpaEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }

            var parameters = getBasicParameters(failedFact);
            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(event.getContextId())
                    .correlationId(event.getCorrelationId())
                    .invocationId(invocationId)
                    .type(InvocationType.JPA)
                    .success(executedTimestamp != null)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .className(event.getClassName())
                    .methodName(event.getMethodName())
                    .parameters(parameters)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationId, trace);
        }
        return callTracesByStartId;
    }

    private static Map<String, CallTrace> readServiceEvents(RecordedEvents events) {
        var serviceEvents = events.getServiceMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<String, CallTrace>();
        for (var event : serviceEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }

            var parameters = getBasicParameters(failedFact);
            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(event.getContextId())
                    .correlationId(event.getCorrelationId())
                    .invocationId(invocationId)
                    .type(InvocationType.SERVICE)
                    .success(executedTimestamp != null)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .className(event.getClassName())
                    .methodName(event.getMethodName())
                    .parameters(parameters)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationId, trace);
        }
        return callTracesByStartId;
    }

    private static Map<String, CallTrace> readComponentEvents(RecordedEvents events) {
        var componentEvents = events.getComponentMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<String, CallTrace>();
        for (var event : componentEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }

            var parameters = getBasicParameters(failedFact);
            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(event.getContextId())
                    .correlationId(event.getCorrelationId())
                    .invocationId(invocationId)
                    .type(InvocationType.COMPONENT)
                    .success(executedTimestamp != null)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .className(event.getClassName())
                    .methodName(event.getMethodName())
                    .parameters(parameters)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationId, trace);
        }
        return callTracesByStartId;
    }

    private static Map<String, CallTrace> readControllerEvents(RecordedEvents events) {
        var controllerEvents = events.getControllerMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<String, CallTrace>();
        for (var event : controllerEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }

            var parameters = getBasicParameters(failedFact);
            parameters.put("HTTP Method", event.getHttpMethod());
            parameters.put("HTTP URL", event.getUrl());
            parameters.put("Rest", String.valueOf(event.isRest()));

            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(event.getContextId())
                    .correlationId(null)
                    .invocationId(invocationId)
                    .type(InvocationType.CONTROLLER)
                    .success(executedTimestamp != null)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .className(event.getClassName())
                    .methodName(event.getMethodName())
                    .parameters(parameters)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationId, trace);
        }
        return callTracesByStartId;
    }

    private static void sortTracesRecursively(List<CallTrace> traces) {
        traces.sort(Comparator.comparing(CallTrace::getStartTime));
        for (var trace : traces) {
            sortTracesRecursively(trace.getChildren());
        }
    }

    static Map<String, String> getBasicParameters(InvocationFailedRecordedEvent failedFact) {
        var parameters = new LinkedHashMap<String, String>();
        if (failedFact != null) {
            parameters.put("Exception Class", failedFact.getExceptionClass());
            parameters.put("Exception Message", failedFact.getExceptionMessage());
        }
        return parameters;
    }

}
