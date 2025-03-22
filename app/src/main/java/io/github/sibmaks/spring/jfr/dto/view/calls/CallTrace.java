package io.github.sibmaks.spring.jfr.dto.view.calls;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public record CallTrace(
        @JsonIgnore
        long contextId,
        @JsonIgnore
        Long correlationId,
        long invocationId,
        int success,
        long type,
        long startTime,
        long endTime,
        long threadName,
        long className,
        long methodName,
        Map<Long, Long> details,
        List<CallTrace> children
) {
    public static CallTraceBuilder builder() {
        return new CallTraceBuilder();
    }

    public static class CallTraceBuilder {
        private long contextId;
        private Long correlationId;
        private long invocationId;
        private int success;
        private long type;
        private long startTime;
        private long endTime;
        private long threadName;
        private long className;
        private long methodName;
        private Map<Long, Long> details;
        private List<CallTrace.CallTraceBuilder> children;

        CallTraceBuilder() {
        }

        public CallTraceBuilder contextId(long contextId) {
            this.contextId = contextId;
            return this;
        }

        public CallTraceBuilder correlationId(Long correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public CallTraceBuilder invocationId(long invocationId) {
            this.invocationId = invocationId;
            return this;
        }

        public CallTraceBuilder success(int success) {
            this.success = success;
            return this;
        }

        public CallTraceBuilder type(long type) {
            this.type = type;
            return this;
        }

        public CallTraceBuilder startTime(long startTime) {
            this.startTime = startTime;
            return this;
        }

        public CallTraceBuilder endTime(long endTime) {
            this.endTime = endTime;
            return this;
        }

        public CallTraceBuilder threadName(long threadName) {
            this.threadName = threadName;
            return this;
        }

        public CallTraceBuilder className(long className) {
            this.className = className;
            return this;
        }

        public CallTraceBuilder methodName(long methodName) {
            this.methodName = methodName;
            return this;
        }

        public CallTraceBuilder details(Map<Long, Long> details) {
            this.details = details;
            return this;
        }

        public CallTraceBuilder addDetails(Map<Long, Long> details) {
            if (this.details == null) {
                this.details = new LinkedHashMap<>();
            }
            this.details.putAll(details);
            return this;
        }

        public CallTraceBuilder children(List<CallTrace.CallTraceBuilder> children) {
            this.children = children;
            return this;
        }

        public CallTraceBuilder child(CallTrace.CallTraceBuilder child) {
            this.children.add(child);
            return this;
        }

        public CallTrace build() {
            var children = this.children == null ? null : this.children.stream()
                    .map(CallTrace.CallTraceBuilder::build)
                    .toList();

            return new CallTrace(
                    this.contextId,
                    this.correlationId,
                    this.invocationId,
                    this.success,
                    this.type,
                    this.startTime,
                    this.endTime,
                    this.threadName,
                    this.className,
                    this.methodName,
                    this.details,
                    children
            );
        }

        public String toString() {
            return "CallTrace.CallTraceBuilder(contextId=" + this.contextId + ", correlationId=" + this.correlationId + ", invocationId=" + this.invocationId + ", success=" + this.success + ", type=" + this.type + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", threadName=" + this.threadName + ", className=" + this.className + ", methodName=" + this.methodName + ", details=" + this.details + ", children=" + this.children + ")";
        }
    }
}
