// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: connections.proto
// Protobuf Java Version: 4.29.3

package io.github.sibmaks.spring.jfr.dto.protobuf.connections;

public interface ConnectionMapOrBuilder extends
    // @@protoc_insertion_point(interface_extends:io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionMap)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>map&lt;int64, .io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList&gt; connections = 1;</code>
   */
  int getConnectionsCount();
  /**
   * <code>map&lt;int64, .io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList&gt; connections = 1;</code>
   */
  boolean containsConnections(
      long key);
  /**
   * Use {@link #getConnectionsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.Long, io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList>
  getConnections();
  /**
   * <code>map&lt;int64, .io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList&gt; connections = 1;</code>
   */
  java.util.Map<java.lang.Long, io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList>
  getConnectionsMap();
  /**
   * <code>map&lt;int64, .io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList&gt; connections = 1;</code>
   */
  /* nullable */
io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList getConnectionsOrDefault(
      long key,
      /* nullable */
io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList defaultValue);
  /**
   * <code>map&lt;int64, .io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList&gt; connections = 1;</code>
   */
  io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList getConnectionsOrThrow(
      long key);
}
