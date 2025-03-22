// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: beans.proto
// Protobuf Java Version: 4.29.3

package io.github.sibmaks.spring.jfr.dto.protobuf.beans;

public interface BeanInitializedOrBuilder extends
    // @@protoc_insertion_point(interface_extends:io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 context_id = 1;</code>
   * @return The contextId.
   */
  long getContextId();

  /**
   * <code>int64 bean_name = 2;</code>
   * @return The beanName.
   */
  long getBeanName();

  /**
   * <code>optional int64 pre_initialized_at = 3;</code>
   * @return Whether the preInitializedAt field is set.
   */
  boolean hasPreInitializedAt();
  /**
   * <code>optional int64 pre_initialized_at = 3;</code>
   * @return The preInitializedAt.
   */
  long getPreInitializedAt();

  /**
   * <code>optional int64 post_initialized_at = 4;</code>
   * @return Whether the postInitializedAt field is set.
   */
  boolean hasPostInitializedAt();
  /**
   * <code>optional int64 post_initialized_at = 4;</code>
   * @return The postInitializedAt.
   */
  long getPostInitializedAt();

  /**
   * <code>double duration = 5;</code>
   * @return The duration.
   */
  double getDuration();
}
