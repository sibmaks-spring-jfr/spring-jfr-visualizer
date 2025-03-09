package io.github.sibmaks.spring.jfr.report;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedEvents;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallTrace;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallsReport;
import io.github.sibmaks.spring.jfr.dto.view.calls.InvocationType;
import io.github.sibmaks.spring.jfr.event.reading.api.common.InvocationFailedRecordedEvent;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static io.github.sibmaks.spring.jfr.utils.JavaFlightRecorderUtils.getThreadName;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CallsReportCreator {
    private final StringConstantRegistry stringConstantRegistry;

    public CallsReport create(RecordedEvents events) {
        var roots = buildCallTraces(events);

        return CallsReport.builder()
                .roots(roots)
                .build();
    }

    private List<CallTrace> buildCallTraces(RecordedEvents events) {
        var callTracesByStartId = new HashMap<Long, CallTrace>();

        callTracesByStartId.putAll(readControllerEvents(events));
        callTracesByStartId.putAll(readJPAEvents(events));
        callTracesByStartId.putAll(readScheduledEvents(events));
        callTracesByStartId.putAll(readServiceEvents(events));
        callTracesByStartId.putAll(readComponentEvents(events));

        var roots = new ArrayList<CallTrace>();

        for (var trace : callTracesByStartId.values()) {
            var parentId = trace.correlationId();
            if (parentId == null) {
                roots.add(trace);
            } else {
                var parent = callTracesByStartId.get(parentId);
                if (parent != null) {
                    parent.children().add(trace);
                } else {
                    roots.add(trace);
                }
            }
        }

        sortTracesRecursively(roots);

        return roots;
    }

    private Map<Long, CallTrace> readScheduledEvents(RecordedEvents events) {
        var scheduledEvents = events.getScheduledMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<Long, CallTrace>();
        for (var event : scheduledEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }
            var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);

            var details = getBasicDetails(failedFact);
            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(stringConstantRegistry.getOrRegister(event.getContextId()))
                    .correlationId(null)
                    .invocationId(invocationIdCode)
                    .type(stringConstantRegistry.getOrRegister(InvocationType.SCHEDULED.name()))
                    .success(executedTimestamp != null ? 1 : 0)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                    .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                    .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                    .details(details)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationIdCode, trace);
        }
        return callTracesByStartId;
    }

    private Map<Long, CallTrace> readJPAEvents(RecordedEvents events) {
        var jpaEvents = events.getJpaMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<Long, CallTrace>();
        for (var event : jpaEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }
            var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);

            var details = getBasicDetails(failedFact);
            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(stringConstantRegistry.getOrRegister(event.getContextId()))
                    .correlationId(stringConstantRegistry.getOrRegister(event.getCorrelationId()))
                    .invocationId(invocationIdCode)
                    .type(stringConstantRegistry.getOrRegister(InvocationType.JPA.name()))
                    .success(executedTimestamp != null ? 1 : 0)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                    .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                    .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                    .details(details)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationIdCode, trace);
        }
        return callTracesByStartId;
    }

    private Map<Long, CallTrace> readServiceEvents(RecordedEvents events) {
        var serviceEvents = events.getServiceMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<Long, CallTrace>();
        for (var event : serviceEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }
            var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);

            var details = getBasicDetails(failedFact);
            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(stringConstantRegistry.getOrRegister(event.getContextId()))
                    .correlationId(stringConstantRegistry.getOrRegister(event.getCorrelationId()))
                    .invocationId(invocationIdCode)
                    .type(stringConstantRegistry.getOrRegister(InvocationType.SERVICE.name()))
                    .success(executedTimestamp != null ? 1 : 0)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                    .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                    .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                    .details(details)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationIdCode, trace);
        }
        return callTracesByStartId;
    }

    private Map<Long, CallTrace> readComponentEvents(RecordedEvents events) {
        var componentEvents = events.getComponentMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<Long, CallTrace>();
        for (var event : componentEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }
            var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);

            var details = getBasicDetails(failedFact);
            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(stringConstantRegistry.getOrRegister(event.getContextId()))
                    .correlationId(stringConstantRegistry.getOrRegister(event.getCorrelationId()))
                    .invocationId(invocationIdCode)
                    .type(stringConstantRegistry.getOrRegister(InvocationType.COMPONENT.name()))
                    .success(executedTimestamp != null ? 1 : 0)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                    .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                    .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                    .details(details)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationIdCode, trace);
        }
        return callTracesByStartId;
    }

    private Map<Long, CallTrace> readControllerEvents(RecordedEvents events) {
        var controllerEvents = events.getControllerMethodCalledRecordedEvents();
        var executedInvocations = events.getExecutedInvocations();
        var failedInvocations = events.getFailedInvocations();

        var callTracesByStartId = new HashMap<Long, CallTrace>();
        for (var event : controllerEvents) {
            var invocationId = event.getInvocationId();
            var executedTimestamp = executedInvocations.get(invocationId);
            var failedFact = failedInvocations.get(invocationId);
            if (executedTimestamp == null && failedFact == null) {
                continue;
            }
            var invocationIdCode = stringConstantRegistry.getOrRegister(invocationId);

            var details = getBasicDetails(failedFact);
            details.put(stringConstantRegistry.getOrRegister("HTTP Method"), stringConstantRegistry.getOrRegister(event.getHttpMethod()));
            details.put(stringConstantRegistry.getOrRegister("HTTP URL"), stringConstantRegistry.getOrRegister(event.getUrl()));
            details.put(stringConstantRegistry.getOrRegister("Rest"), stringConstantRegistry.getOrRegister(String.valueOf(event.isRest())));

            var endTime = executedTimestamp == null ? failedFact.getStartTime() : executedTimestamp;

            var trace = CallTrace.builder()
                    .contextId(stringConstantRegistry.getOrRegister(event.getContextId()))
                    .correlationId(null)
                    .invocationId(invocationIdCode)
                    .type(stringConstantRegistry.getOrRegister(InvocationType.CONTROLLER.name()))
                    .success(executedTimestamp != null ? 1 : 0)
                    .startTime(event.getStartTime().toEpochMilli())
                    .endTime(endTime.toEpochMilli())
                    .threadName(stringConstantRegistry.getOrRegister(getThreadName(event)))
                    .className(stringConstantRegistry.getOrRegister(event.getClassName()))
                    .methodName(stringConstantRegistry.getOrRegister(event.getMethodName()))
                    .details(details)
                    .children(new ArrayList<>())
                    .build();

            callTracesByStartId.put(invocationIdCode, trace);
        }
        return callTracesByStartId;
    }

    private void sortTracesRecursively(List<CallTrace> traces) {
        traces.sort(Comparator.comparing(CallTrace::startTime));
        for (var trace : traces) {
            sortTracesRecursively(trace.children());
        }
    }

    Map<Long, Long> getBasicDetails(InvocationFailedRecordedEvent failedFact) {
        var details = new LinkedHashMap<Long, Long>();
        if (failedFact != null) {
            details.put(stringConstantRegistry.getOrRegister("Exception Class"), stringConstantRegistry.getOrRegister(failedFact.getExceptionClass()));
            details.put(stringConstantRegistry.getOrRegister("Exception Message"), stringConstantRegistry.getOrRegister(failedFact.getExceptionMessage()));
        }
        return details;
    }

}
