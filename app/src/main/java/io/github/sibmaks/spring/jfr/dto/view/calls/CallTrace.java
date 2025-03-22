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
        int contextId,
        @JsonIgnore
        Integer correlationId,
        int invocationId,
        boolean success,
        int type,
        long startTime,
        long endTime,
        int threadName,
        int className,
        int methodName,
        Map<Integer, Integer> details,
        List<CallTrace> children
) {
    public static CallTraceBuilder builder() {
        return new CallTraceBuilder();
    }

    public static class CallTraceBuilder {
        private int contextId;
        private Integer correlationId;
        private int invocationId;
        private boolean success;
        private int type;
        private long startTime;
        private long endTime;
        private int threadName;
        private int className;
        private int methodName;
        private Map<Integer, Integer> details;
        private List<CallTrace.CallTraceBuilder> children;

        CallTraceBuilder() {
        }

        public CallTraceBuilder contextId(int contextId) {
            this.contextId = contextId;
            return this;
        }

        public CallTraceBuilder correlationId(Integer correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public CallTraceBuilder invocationId(int invocationId) {
            this.invocationId = invocationId;
            return this;
        }

        public CallTraceBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public CallTraceBuilder type(int type) {
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

        public CallTraceBuilder threadName(int threadName) {
            this.threadName = threadName;
            return this;
        }

        public CallTraceBuilder className(int className) {
            this.className = className;
            return this;
        }

        public CallTraceBuilder methodName(int methodName) {
            this.methodName = methodName;
            return this;
        }

        public CallTraceBuilder details(Map<Integer, Integer> details) {
            this.details = details;
            return this;
        }

        public CallTraceBuilder addDetails(Map<Integer, Integer> details) {
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
