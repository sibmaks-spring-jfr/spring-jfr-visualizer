// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: kafka.consumer.proto
// Protobuf Java Version: 4.29.3

package io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer;

public interface PartitionOffsetsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer.PartitionOffsets)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 current_offset = 1;</code>
   * @return The currentOffset.
   */
  long getCurrentOffset();

  /**
   * <code>int64 last_commit = 2;</code>
   * @return The lastCommit.
   */
  long getLastCommit();
}
