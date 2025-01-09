package io.github.sibmaks.spring.jfr.report;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedEvents;
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
            var failedTimestamp = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedTimestamp == null) {
                continue;
            }

            var trace = CallTrace.builder()
                    .contextId(event.getContextId())
                    .correlationId(null)
                    .invocationId(invocationId)
                    .type(InvocationType.SCHEDULED)
                    .success(executedTimestamp != null)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime((executedTimestamp == null ? failedTimestamp : executedTimestamp).toEpochMilli())
                    .parameters(Map.ofEntries(
                            Map.entry("Class Name", event.getClassName()),
                            Map.entry("Method Name", event.getMethodName())
                    ))
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
            var failedTimestamp = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedTimestamp == null) {
                continue;
            }

            var trace = CallTrace.builder()
                    .contextId(event.getContextId())
                    .correlationId(event.getCorrelationId())
                    .invocationId(invocationId)
                    .type(InvocationType.JPA)
                    .success(executedTimestamp != null)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime((executedTimestamp == null ? failedTimestamp : executedTimestamp).toEpochMilli())
                    .parameters(Map.ofEntries(
                            Map.entry("Class Name", event.getClassName()),
                            Map.entry("Method Name", event.getMethodName())
                    ))
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
            var failedTimestamp = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedTimestamp == null) {
                continue;
            }

            var trace = CallTrace.builder()
                    .contextId(event.getContextId())
                    .correlationId(null)
                    .invocationId(invocationId)
                    .type(InvocationType.CONTROLLER)
                    .success(executedTimestamp != null)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime((executedTimestamp == null ? failedTimestamp : executedTimestamp).toEpochMilli())
                    .parameters(Map.ofEntries(
                            Map.entry("HTTP Method", event.getHttpMethod()),
                            Map.entry("HTTP URL", event.getUrl()),
                            Map.entry("Rest", String.valueOf(event.isRest())),
                            Map.entry("Class Name", event.getClassName()),
                            Map.entry("Method Name", event.getMethodName())
                    ))
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

}
