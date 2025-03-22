// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: connections.proto
// Protobuf Java Version: 4.29.3

package io.github.sibmaks.spring.jfr.dto.protobuf.connections;

/**
 * Protobuf type {@code io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList}
 */
public final class ConnectionList extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList)
    ConnectionListOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 29,
      /* patch= */ 3,
      /* suffix= */ "",
      ConnectionList.class.getName());
  }
  // Use ConnectionList.newBuilder() to construct.
  private ConnectionList(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private ConnectionList() {
    connections_ = java.util.Collections.emptyList();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connections.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_connections_ConnectionList_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connections.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_connections_ConnectionList_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList.class, io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList.Builder.class);
  }

  public static final int CONNECTIONS_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private java.util.List<io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection> connections_;
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
   */
  @java.lang.Override
  public java.util.List<io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection> getConnectionsList() {
    return connections_;
  }
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
   */
  @java.lang.Override
  public java.util.List<? extends io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionOrBuilder> 
      getConnectionsOrBuilderList() {
    return connections_;
  }
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
   */
  @java.lang.Override
  public int getConnectionsCount() {
    return connections_.size();
  }
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
   */
  @java.lang.Override
  public io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection getConnections(int index) {
    return connections_.get(index);
  }
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
   */
  @java.lang.Override
  public io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionOrBuilder getConnectionsOrBuilder(
      int index) {
    return connections_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    for (int i = 0; i < connections_.size(); i++) {
      output.writeMessage(1, connections_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < connections_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, connections_.get(i));
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList)) {
      return super.equals(obj);
    }
    io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList other = (io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList) obj;

    if (!getConnectionsList()
        .equals(other.getConnectionsList())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getConnectionsCount() > 0) {
      hash = (37 * hash) + CONNECTIONS_FIELD_NUMBER;
      hash = (53 * hash) + getConnectionsList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList)
      io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionListOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connections.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_connections_ConnectionList_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connections.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_connections_ConnectionList_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList.class, io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList.Builder.class);
    }

    // Construct using io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      if (connectionsBuilder_ == null) {
        connections_ = java.util.Collections.emptyList();
      } else {
        connections_ = null;
        connectionsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connections.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_connections_ConnectionList_descriptor;
    }

    @java.lang.Override
    public io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList getDefaultInstanceForType() {
      return io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList.getDefaultInstance();
    }

    @java.lang.Override
    public io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList build() {
      io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList buildPartial() {
      io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList result = new io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList result) {
      if (connectionsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          connections_ = java.util.Collections.unmodifiableList(connections_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.connections_ = connections_;
      } else {
        result.connections_ = connectionsBuilder_.build();
      }
    }

    private void buildPartial0(io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList result) {
      int from_bitField0_ = bitField0_;
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList) {
        return mergeFrom((io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList other) {
      if (other == io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList.getDefaultInstance()) return this;
      if (connectionsBuilder_ == null) {
        if (!other.connections_.isEmpty()) {
          if (connections_.isEmpty()) {
            connections_ = other.connections_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureConnectionsIsMutable();
            connections_.addAll(other.connections_);
          }
          onChanged();
        }
      } else {
        if (!other.connections_.isEmpty()) {
          if (connectionsBuilder_.isEmpty()) {
            connectionsBuilder_.dispose();
            connectionsBuilder_ = null;
            connections_ = other.connections_;
            bitField0_ = (bitField0_ & ~0x00000001);
            connectionsBuilder_ = 
              com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                 getConnectionsFieldBuilder() : null;
          } else {
            connectionsBuilder_.addAllMessages(other.connections_);
          }
        }
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection m =
                  input.readMessage(
                      io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.parser(),
                      extensionRegistry);
              if (connectionsBuilder_ == null) {
                ensureConnectionsIsMutable();
                connections_.add(m);
              } else {
                connectionsBuilder_.addMessage(m);
              }
              break;
            } // case 10
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.util.List<io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection> connections_ =
      java.util.Collections.emptyList();
    private void ensureConnectionsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        connections_ = new java.util.ArrayList<io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection>(connections_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilder<
        io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection, io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder, io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionOrBuilder> connectionsBuilder_;

    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public java.util.List<io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection> getConnectionsList() {
      if (connectionsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(connections_);
      } else {
        return connectionsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public int getConnectionsCount() {
      if (connectionsBuilder_ == null) {
        return connections_.size();
      } else {
        return connectionsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection getConnections(int index) {
      if (connectionsBuilder_ == null) {
        return connections_.get(index);
      } else {
        return connectionsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder setConnections(
        int index, io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection value) {
      if (connectionsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureConnectionsIsMutable();
        connections_.set(index, value);
        onChanged();
      } else {
        connectionsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder setConnections(
        int index, io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder builderForValue) {
      if (connectionsBuilder_ == null) {
        ensureConnectionsIsMutable();
        connections_.set(index, builderForValue.build());
        onChanged();
      } else {
        connectionsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder addConnections(io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection value) {
      if (connectionsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureConnectionsIsMutable();
        connections_.add(value);
        onChanged();
      } else {
        connectionsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder addConnections(
        int index, io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection value) {
      if (connectionsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureConnectionsIsMutable();
        connections_.add(index, value);
        onChanged();
      } else {
        connectionsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder addConnections(
        io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder builderForValue) {
      if (connectionsBuilder_ == null) {
        ensureConnectionsIsMutable();
        connections_.add(builderForValue.build());
        onChanged();
      } else {
        connectionsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder addConnections(
        int index, io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder builderForValue) {
      if (connectionsBuilder_ == null) {
        ensureConnectionsIsMutable();
        connections_.add(index, builderForValue.build());
        onChanged();
      } else {
        connectionsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder addAllConnections(
        java.lang.Iterable<? extends io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection> values) {
      if (connectionsBuilder_ == null) {
        ensureConnectionsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, connections_);
        onChanged();
      } else {
        connectionsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder clearConnections() {
      if (connectionsBuilder_ == null) {
        connections_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        connectionsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public Builder removeConnections(int index) {
      if (connectionsBuilder_ == null) {
        ensureConnectionsIsMutable();
        connections_.remove(index);
        onChanged();
      } else {
        connectionsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder getConnectionsBuilder(
        int index) {
      return getConnectionsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionOrBuilder getConnectionsOrBuilder(
        int index) {
      if (connectionsBuilder_ == null) {
        return connections_.get(index);  } else {
        return connectionsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public java.util.List<? extends io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionOrBuilder> 
         getConnectionsOrBuilderList() {
      if (connectionsBuilder_ != null) {
        return connectionsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(connections_);
      }
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder addConnectionsBuilder() {
      return getConnectionsFieldBuilder().addBuilder(
          io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.getDefaultInstance());
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder addConnectionsBuilder(
        int index) {
      return getConnectionsFieldBuilder().addBuilder(
          index, io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.getDefaultInstance());
    }
    /**
     * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection connections = 1;</code>
     */
    public java.util.List<io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder> 
         getConnectionsBuilderList() {
      return getConnectionsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilder<
        io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection, io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder, io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionOrBuilder> 
        getConnectionsFieldBuilder() {
      if (connectionsBuilder_ == null) {
        connectionsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
            io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection, io.github.sibmaks.spring.jfr.dto.protobuf.connections.Connection.Builder, io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionOrBuilder>(
                connections_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        connections_ = null;
      }
      return connectionsBuilder_;
    }

    // @@protoc_insertion_point(builder_scope:io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList)
  }

  // @@protoc_insertion_point(class_scope:io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList)
  private static final io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList();
  }

  public static io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ConnectionList>
      PARSER = new com.google.protobuf.AbstractParser<ConnectionList>() {
    @java.lang.Override
    public ConnectionList parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<ConnectionList> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ConnectionList> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public io.github.sibmaks.spring.jfr.dto.protobuf.connections.ConnectionList getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

