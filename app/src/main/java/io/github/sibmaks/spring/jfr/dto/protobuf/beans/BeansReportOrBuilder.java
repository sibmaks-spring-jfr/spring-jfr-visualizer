// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: beans.proto
// Protobuf Java Version: 4.29.3

package io.github.sibmaks.spring.jfr.dto.protobuf.beans;

public interface BeansReportOrBuilder extends
    // @@protoc_insertion_point(interface_extends:io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeansReport)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>map&lt;int32, .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList&gt; bean_definitions = 1;</code>
   */
  int getBeanDefinitionsCount();
  /**
   * <code>map&lt;int32, .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList&gt; bean_definitions = 1;</code>
   */
  boolean containsBeanDefinitions(
      int key);
  /**
   * Use {@link #getBeanDefinitionsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.Integer, io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList>
  getBeanDefinitions();
  /**
   * <code>map&lt;int32, .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList&gt; bean_definitions = 1;</code>
   */
  java.util.Map<java.lang.Integer, io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList>
  getBeanDefinitionsMap();
  /**
   * <code>map&lt;int32, .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList&gt; bean_definitions = 1;</code>
   */
  /* nullable */
io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList getBeanDefinitionsOrDefault(
      int key,
      /* nullable */
io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList defaultValue);
  /**
   * <code>map&lt;int32, .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList&gt; bean_definitions = 1;</code>
   */
  io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanDefinitionList getBeanDefinitionsOrThrow(
      int key);

  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized beans = 2;</code>
   */
  java.util.List<io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized> 
      getBeansList();
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized beans = 2;</code>
   */
  io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized getBeans(int index);
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized beans = 2;</code>
   */
  int getBeansCount();
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized beans = 2;</code>
   */
  java.util.List<? extends io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitializedOrBuilder> 
      getBeansOrBuilderList();
  /**
   * <code>repeated .io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitialized beans = 2;</code>
   */
  io.github.sibmaks.spring.jfr.dto.protobuf.beans.BeanInitializedOrBuilder getBeansOrBuilder(
      int index);
}
