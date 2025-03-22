// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: calls.proto
// Protobuf Java Version: 4.29.3

package io.github.sibmaks.spring.jfr.dto.protobuf.calls;

public interface CallTraceOrBuilder extends
    // @@protoc_insertion_point(interface_extends:io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 invocation_id = 1;</code>
   * @return The invocationId.
   */
  long getInvocationId();

  /**
   * <code>int32 success = 2;</code>
   * @return The success.
   */
  int getSuccess();

  /**
   * <code>int64 type = 3;</code>
   * @return The type.
   */
  long getType();

  /**
   * <code>int64 start_time = 4;</code>
   * @return The startTime.
   */
  long getStartTime();

  /**
   * <code>int64 end_time = 5;</code>
   * @return The endTime.
   */
  long getEndTime();

  /**
   * <code>int64 thread_name = 6;</code>
   * @return The threadName.
   */
  long getThreadName();

  /**
   * <code>int64 class_name = 7;</code>
   * @return The className.
   */
  long getClassName();

  /**
   * <code>int64 method_name = 8;</code>
   * @return The methodName.
   */
  long getMethodName();

  /**
   * <code>map&lt;int64, int64&gt; details = 9;</code>
   */
  int getDetailsCount();
  /**
   * <code>map&lt;int64, int64&gt; details = 9;</code>
   */
  boolean containsDetails(
      long key);
  /**
   * Use {@link #getDetailsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.Long, java.lang.Long>
  getDetails();
  /**
   * <code>map&lt;int64, int64&gt; details = 9;</code>
   */
  java.util.Map<java.lang.Long, java.lang.Long>
  getDetailsMap();
  /**
   * <code>map&lt;int64, int64&gt; details = 9;</code>
   */
  long getDetailsOrDefault(
      long key,
      long defaultValue);
  /**
   * <code>map&lt;int64, int64&gt; details = 9;</code>
   */
  long getDetailsOrThrow(
      long key);

  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace children = 10;</code>
   */
  java.util.List<io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace> 
      getChildrenList();
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace children = 10;</code>
   */
  io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace getChildren(int index);
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace children = 10;</code>
   */
  int getChildrenCount();
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace children = 10;</code>
   */
  java.util.List<? extends io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTraceOrBuilder> 
      getChildrenOrBuilderList();
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTrace children = 10;</code>
   */
  io.github.sibmaks.spring.jfr.dto.protobuf.calls.CallTraceOrBuilder getChildrenOrBuilder(
      int index);
}
