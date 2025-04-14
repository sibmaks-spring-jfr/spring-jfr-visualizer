// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: kafka.consumer.proto
// Protobuf Java Version: 4.29.3

package io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer;

public interface KafkaConsumerPartitionEventOrBuilder extends
    // @@protoc_insertion_point(interface_extends:io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer.KafkaConsumerPartitionEvent)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer.KafkaConsumerPartitionEventType eventType = 1;</code>
   * @return The enum numeric value on the wire for eventType.
   */
  int getEventTypeValue();
  /**
   * <code>.io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer.KafkaConsumerPartitionEventType eventType = 1;</code>
   * @return The eventType.
   */
  io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer.KafkaConsumerPartitionEventType getEventType();

  /**
   * <code>repeated int32 partitions = 2;</code>
   * @return A list containing the partitions.
   */
  java.util.List<java.lang.Integer> getPartitionsList();
  /**
   * <code>repeated int32 partitions = 2;</code>
   * @return The count of partitions.
   */
  int getPartitionsCount();
  /**
   * <code>repeated int32 partitions = 2;</code>
   * @param index The index of the element to return.
   * @return The partitions at the given index.
   */
  int getPartitions(int index);

  /**
   * <code>int64 at = 3;</code>
   * @return The at.
   */
  long getAt();
}
