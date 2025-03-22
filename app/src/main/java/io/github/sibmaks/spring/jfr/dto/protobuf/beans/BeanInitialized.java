// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: beans.proto
// Protobuf Java Version: 4.29.3

package io.github.sibmaks.spring.jfr.dto.protobuf.beans;

/**
 * Protobuf type {@code io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized}
 */
public final class BeanInitialized extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized)
    BeanInitializedOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 29,
      /* patch= */ 3,
      /* suffix= */ "",
      BeanInitialized.class.getName());
  }
  // Use BeanInitialized.newBuilder() to construct.
  private BeanInitialized(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private BeanInitialized() {
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return io.github.sibmaks.spring.jfr.dto.protobuf.beans.Beans.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_beans_BeanInitialized_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return io.github.sibmaks.spring.jfr.dto.protobuf.beans.Beans.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_beans_BeanInitialized_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized.class, io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized.Builder.class);
  }

  private int bitField0_;
  public static final int CONTEXT_ID_FIELD_NUMBER = 1;
  private long contextId_ = 0L;
  /**
   * <code>int64 context_id = 1;</code>
   * @return The contextId.
   */
  @java.lang.Override
  public long getContextId() {
    return contextId_;
  }

  public static final int BEAN_NAME_FIELD_NUMBER = 2;
  private long beanName_ = 0L;
  /**
   * <code>int64 bean_name = 2;</code>
   * @return The beanName.
   */
  @java.lang.Override
  public long getBeanName() {
    return beanName_;
  }

  public static final int PRE_INITIALIZED_AT_FIELD_NUMBER = 3;
  private long preInitializedAt_ = 0L;
  /**
   * <code>optional int64 pre_initialized_at = 3;</code>
   * @return Whether the preInitializedAt field is set.
   */
  @java.lang.Override
  public boolean hasPreInitializedAt() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>optional int64 pre_initialized_at = 3;</code>
   * @return The preInitializedAt.
   */
  @java.lang.Override
  public long getPreInitializedAt() {
    return preInitializedAt_;
  }

  public static final int POST_INITIALIZED_AT_FIELD_NUMBER = 4;
  private long postInitializedAt_ = 0L;
  /**
   * <code>optional int64 post_initialized_at = 4;</code>
   * @return Whether the postInitializedAt field is set.
   */
  @java.lang.Override
  public boolean hasPostInitializedAt() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>optional int64 post_initialized_at = 4;</code>
   * @return The postInitializedAt.
   */
  @java.lang.Override
  public long getPostInitializedAt() {
    return postInitializedAt_;
  }

  public static final int DURATION_FIELD_NUMBER = 5;
  private double duration_ = 0D;
  /**
   * <code>double duration = 5;</code>
   * @return The duration.
   */
  @java.lang.Override
  public double getDuration() {
    return duration_;
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
    if (contextId_ != 0L) {
      output.writeInt64(1, contextId_);
    }
    if (beanName_ != 0L) {
      output.writeInt64(2, beanName_);
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeInt64(3, preInitializedAt_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeInt64(4, postInitializedAt_);
    }
    if (java.lang.Double.doubleToRawLongBits(duration_) != 0) {
      output.writeDouble(5, duration_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (contextId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, contextId_);
    }
    if (beanName_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, beanName_);
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, preInitializedAt_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(4, postInitializedAt_);
    }
    if (java.lang.Double.doubleToRawLongBits(duration_) != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeDoubleSize(5, duration_);
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
    if (!(obj instanceof io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized)) {
      return super.equals(obj);
    }
    io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized other = (io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized) obj;

    if (getContextId()
        != other.getContextId()) return false;
    if (getBeanName()
        != other.getBeanName()) return false;
    if (hasPreInitializedAt() != other.hasPreInitializedAt()) return false;
    if (hasPreInitializedAt()) {
      if (getPreInitializedAt()
          != other.getPreInitializedAt()) return false;
    }
    if (hasPostInitializedAt() != other.hasPostInitializedAt()) return false;
    if (hasPostInitializedAt()) {
      if (getPostInitializedAt()
          != other.getPostInitializedAt()) return false;
    }
    if (java.lang.Double.doubleToLongBits(getDuration())
        != java.lang.Double.doubleToLongBits(
            other.getDuration())) return false;
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
    hash = (37 * hash) + CONTEXT_ID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getContextId());
    hash = (37 * hash) + BEAN_NAME_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getBeanName());
    if (hasPreInitializedAt()) {
      hash = (37 * hash) + PRE_INITIALIZED_AT_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getPreInitializedAt());
    }
    if (hasPostInitializedAt()) {
      hash = (37 * hash) + POST_INITIALIZED_AT_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getPostInitializedAt());
    }
    hash = (37 * hash) + DURATION_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        java.lang.Double.doubleToLongBits(getDuration()));
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized parseFrom(
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
  public static Builder newBuilder(io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized prototype) {
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
   * Protobuf type {@code io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized)
      io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitializedOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return io.github.sibmaks.spring.jfr.dto.protobuf.beans.Beans.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_beans_BeanInitialized_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return io.github.sibmaks.spring.jfr.dto.protobuf.beans.Beans.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_beans_BeanInitialized_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized.class, io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized.Builder.class);
    }

    // Construct using io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized.newBuilder()
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
      contextId_ = 0L;
      beanName_ = 0L;
      preInitializedAt_ = 0L;
      postInitializedAt_ = 0L;
      duration_ = 0D;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return io.github.sibmaks.spring.jfr.dto.protobuf.beans.Beans.internal_static_io_github_sibmaks_spring_jfr_dto_protobuf_beans_BeanInitialized_descriptor;
    }

    @java.lang.Override
    public io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized getDefaultInstanceForType() {
      return io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized.getDefaultInstance();
    }

    @java.lang.Override
    public io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized build() {
      io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized buildPartial() {
      io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized result = new io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.contextId_ = contextId_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.beanName_ = beanName_;
      }
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.preInitializedAt_ = preInitializedAt_;
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.postInitializedAt_ = postInitializedAt_;
        to_bitField0_ |= 0x00000002;
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
        result.duration_ = duration_;
      }
      result.bitField0_ |= to_bitField0_;
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized) {
        return mergeFrom((io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized other) {
      if (other == io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized.getDefaultInstance()) return this;
      if (other.getContextId() != 0L) {
        setContextId(other.getContextId());
      }
      if (other.getBeanName() != 0L) {
        setBeanName(other.getBeanName());
      }
      if (other.hasPreInitializedAt()) {
        setPreInitializedAt(other.getPreInitializedAt());
      }
      if (other.hasPostInitializedAt()) {
        setPostInitializedAt(other.getPostInitializedAt());
      }
      if (other.getDuration() != 0D) {
        setDuration(other.getDuration());
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
            case 8: {
              contextId_ = input.readInt64();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 16: {
              beanName_ = input.readInt64();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              preInitializedAt_ = input.readInt64();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 32: {
              postInitializedAt_ = input.readInt64();
              bitField0_ |= 0x00000008;
              break;
            } // case 32
            case 41: {
              duration_ = input.readDouble();
              bitField0_ |= 0x00000010;
              break;
            } // case 41
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

    private long contextId_ ;
    /**
     * <code>int64 context_id = 1;</code>
     * @return The contextId.
     */
    @java.lang.Override
    public long getContextId() {
      return contextId_;
    }
    /**
     * <code>int64 context_id = 1;</code>
     * @param value The contextId to set.
     * @return This builder for chaining.
     */
    public Builder setContextId(long value) {

      contextId_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>int64 context_id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearContextId() {
      bitField0_ = (bitField0_ & ~0x00000001);
      contextId_ = 0L;
      onChanged();
      return this;
    }

    private long beanName_ ;
    /**
     * <code>int64 bean_name = 2;</code>
     * @return The beanName.
     */
    @java.lang.Override
    public long getBeanName() {
      return beanName_;
    }
    /**
     * <code>int64 bean_name = 2;</code>
     * @param value The beanName to set.
     * @return This builder for chaining.
     */
    public Builder setBeanName(long value) {

      beanName_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>int64 bean_name = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearBeanName() {
      bitField0_ = (bitField0_ & ~0x00000002);
      beanName_ = 0L;
      onChanged();
      return this;
    }

    private long preInitializedAt_ ;
    /**
     * <code>optional int64 pre_initialized_at = 3;</code>
     * @return Whether the preInitializedAt field is set.
     */
    @java.lang.Override
    public boolean hasPreInitializedAt() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <code>optional int64 pre_initialized_at = 3;</code>
     * @return The preInitializedAt.
     */
    @java.lang.Override
    public long getPreInitializedAt() {
      return preInitializedAt_;
    }
    /**
     * <code>optional int64 pre_initialized_at = 3;</code>
     * @param value The preInitializedAt to set.
     * @return This builder for chaining.
     */
    public Builder setPreInitializedAt(long value) {

      preInitializedAt_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>optional int64 pre_initialized_at = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearPreInitializedAt() {
      bitField0_ = (bitField0_ & ~0x00000004);
      preInitializedAt_ = 0L;
      onChanged();
      return this;
    }

    private long postInitializedAt_ ;
    /**
     * <code>optional int64 post_initialized_at = 4;</code>
     * @return Whether the postInitializedAt field is set.
     */
    @java.lang.Override
    public boolean hasPostInitializedAt() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <code>optional int64 post_initialized_at = 4;</code>
     * @return The postInitializedAt.
     */
    @java.lang.Override
    public long getPostInitializedAt() {
      return postInitializedAt_;
    }
    /**
     * <code>optional int64 post_initialized_at = 4;</code>
     * @param value The postInitializedAt to set.
     * @return This builder for chaining.
     */
    public Builder setPostInitializedAt(long value) {

      postInitializedAt_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>optional int64 post_initialized_at = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearPostInitializedAt() {
      bitField0_ = (bitField0_ & ~0x00000008);
      postInitializedAt_ = 0L;
      onChanged();
      return this;
    }

    private double duration_ ;
    /**
     * <code>double duration = 5;</code>
     * @return The duration.
     */
    @java.lang.Override
    public double getDuration() {
      return duration_;
    }
    /**
     * <code>double duration = 5;</code>
     * @param value The duration to set.
     * @return This builder for chaining.
     */
    public Builder setDuration(double value) {

      duration_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    /**
     * <code>double duration = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearDuration() {
      bitField0_ = (bitField0_ & ~0x00000010);
      duration_ = 0D;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized)
  }

  // @@protoc_insertion_point(class_scope:io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized)
  private static final io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized();
  }

  public static io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<BeanInitialized>
      PARSER = new com.google.protobuf.AbstractParser<BeanInitialized>() {
    @java.lang.Override
    public BeanInitialized parsePartialFrom(
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

  public static com.google.protobuf.Parser<BeanInitialized> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<BeanInitialized> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

