package io.github.sibmaks.spring.jfr.report.kafka.consumer;

import io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer.*;
import io.github.sibmaks.spring.jfr.event.api.kafka.consumer.commit.KafkaConsumerCommitFact;
import io.github.sibmaks.spring.jfr.event.reading.api.kafka.consumer.KafkaConsumerCreatedRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.kafka.consumer.commit.KafkaConsumerCommitFailedRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.kafka.consumer.commit.KafkaConsumerCommitRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.kafka.consumer.commit.KafkaConsumerCommitedRecordedEvent;
import io.github.sibmaks.spring.jfr.event.reading.api.kafka.consumer.topic.KafkaConsumerTopicsSubscribedRecordedEvent;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Slf4j
@Component
public class KafkaConsumersReportCreator {
    private final Map<Integer, Map<Integer, KafkaConsumer.Builder>> contexts;
    private final Map<Integer, Map<Integer, PartitionOffsets.Builder>> consumerPartitionOffsets;
    private final Map<Integer, KafkaConsumer.Builder> consumers;
    private final Map<Integer, KafkaConsumerStats.Builder> consumersStats;
    private final Map<String, KafkaConsumerCommitFact> commits;
    private final StringConstantRegistry stringConstantRegistry;

    public KafkaConsumersReportCreator(StringConstantRegistry stringConstantRegistry) {
        this.contexts = new HashMap<>();
        this.consumerPartitionOffsets = new HashMap<>();
        this.consumers = new HashMap<>();
        this.consumersStats = new HashMap<>();
        this.commits = new HashMap<>();
        this.stringConstantRegistry = stringConstantRegistry;
    }

    @EventListener
    public void onKafkaConsumerCreated(KafkaConsumerCreatedRecordedEvent event) {
        try {
            var contextId = stringConstantRegistry.getOrRegister(event.getContextId());

            var consumerFactoryId = stringConstantRegistry.getOrRegister(event.getConsumerFactory());

            var consumerId = stringConstantRegistry.getOrRegister(event.getConsumerId());

            var consumerGroup = stringConstantRegistry.getOrRegister(event.getConsumerGroup());

            var bootstrapServers = stringConstantRegistry.getOrRegister(event.getBootstrapServers());

            var topics = Arrays.stream(event.getTopicsAsArray())
                    .map(stringConstantRegistry::getOrRegister)
                    .toList();

            var kafkaConsumer = KafkaConsumer.newBuilder()
                    .setConsumerFactory(consumerFactoryId)
                    .setConsumerId(consumerId)
                    .setBootstrapServers(bootstrapServers)
                    .setConsumerGroup(consumerGroup)
                    .addAllTopics(topics);

            var contextConsumers = contexts.computeIfAbsent(contextId, k -> new HashMap<>());
            contextConsumers.put(consumerId, kafkaConsumer);
            consumers.put(consumerId, kafkaConsumer);
        } catch (Exception e) {
            log.error("KafkaConsumerCreatedRecordedEvent processing error", e);
        }
    }

    @EventListener
    public void onKafkaConsumerTopicsSubscribed(KafkaConsumerTopicsSubscribedRecordedEvent event) {
        try {
            var consumerId = stringConstantRegistry.getOrRegister(event.getConsumerId());
            var consumerBuilder = consumers.get(consumerId);
            if (consumerBuilder == null) {
                log.warn("Consumer with id {} not found", consumerId);
                return;
            }
            var topics = new HashSet<>(consumerBuilder.getTopicsList());
            topics.addAll(
                    Arrays.stream(event.getTopicsAsArray())
                            .map(stringConstantRegistry::getOrRegister)
                            .toList()
            );
            consumerBuilder.clearTopics();
            consumerBuilder.addAllTopics(topics);
        } catch (Exception e) {
            log.error("KafkaConsumerTopicsSubscribedRecordedEvent processing error", e);
        }
    }

    @EventListener
    public void onKafkaConsumerCommit(KafkaConsumerCommitRecordedEvent event) {
        try {
            commits.put(
                    event.getCommitId(),
                    event
            );
            var consumerId = stringConstantRegistry.getOrRegister(event.getConsumerId());
            var stats = consumersStats.computeIfAbsent(consumerId, it -> KafkaConsumerStats.newBuilder());
            stats
                    .setCommits(stats.getCommits() + 1);
        } catch (Exception e) {
            log.error("KafkaConsumerCommitRecordedEvent processing error", e);
        }
    }

    @EventListener
    public void onKafkaConsumerCommited(KafkaConsumerCommitedRecordedEvent event) {
        try {
            var commitFact = commits.remove(event.getCommitId());
            if (commitFact == null) {
                log.warn("Commit with id {} not found", event.getCommitId());
                return;
            }
            var consumerId = stringConstantRegistry.getOrRegister(commitFact.getConsumerId());

            var stats = consumersStats.computeIfAbsent(consumerId, it -> KafkaConsumerStats.newBuilder());
            stats
                    .setCommited(stats.getCommited() + 1)
                    .setLastCommitAt(Math.max(stats.getLastCommitAt(), event.getEndTime().toEpochMilli()));

            var partitionBuilders = consumerPartitionOffsets.computeIfAbsent(consumerId, it -> new HashMap<>());
            var offsetsAsMap = commitFact.getOffsetsAsMap();
            for (var entry : offsetsAsMap.entrySet()) {
                var key = entry.getKey();
                var value = Long.parseLong(entry.getValue());
                var partition = stringConstantRegistry.getOrRegister("%s-%d".formatted(key.getKey(), key.getValue()));
                var partitionBuilder = partitionBuilders.computeIfAbsent(partition, it -> PartitionOffsets.newBuilder());
                partitionBuilder.setLastCommit(Math.max(partitionBuilder.getLastCommit(), value));
            }
        } catch (Exception e) {
            log.error("KafkaConsumerCommitedRecordedEvent processing error", e);
        }
    }

    @EventListener
    public void onKafkaConsumerCommitFailed(KafkaConsumerCommitFailedRecordedEvent event) {
        try {
            var commitFact = commits.remove(event.getCommitId());
            if (commitFact == null) {
                log.warn("Commit with id {} not found", event.getCommitId());
                return;
            }
            var consumerId = stringConstantRegistry.getOrRegister(commitFact.getConsumerId());
            var stats = consumersStats.computeIfAbsent(consumerId, it -> KafkaConsumerStats.newBuilder());
            stats
                    .setCommitFailed(stats.getCommitFailed() + 1);
        } catch (Exception e) {
            log.error("KafkaConsumerCommitFailedRecordedEvent processing error", e);
        }
    }

    public KafkaConsumersReport get() {
        var kafkaConsumersReport = KafkaConsumersReport.newBuilder();
        for (var entry : contexts.entrySet()) {
            var value = entry.getValue();
            var kafkaMap = KafkaConsumersMap.newBuilder()
                    .putAllConsumers(
                            value.entrySet()
                                    .stream()
                                    .collect(Collectors.toMap(Map.Entry::getKey, this::buildConsumer))
                    )
                    .build();
            kafkaConsumersReport.putContexts(entry.getKey(), kafkaMap);
        }
        return kafkaConsumersReport.build();
    }

    private KafkaConsumer buildConsumer(Map.Entry<Integer, KafkaConsumer.Builder> entry) {
        var builder = entry.getValue();

        var stats = consumersStats.get(builder.getConsumerId());
        if (stats != null) {
            builder.setStats(stats);
        }

        var partitions = consumerPartitionOffsets.get(builder.getConsumerId());
        if (partitions == null || partitions.isEmpty()) {
            return builder.build();
        }

        builder.putAllPartitions(
                partitions.entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, it -> it.getValue().build()))
        );

        return builder.build();
    }

}
