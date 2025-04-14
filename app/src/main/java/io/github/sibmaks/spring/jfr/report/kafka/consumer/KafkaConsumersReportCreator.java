package io.github.sibmaks.spring.jfr.report.kafka.consumer;

import io.github.sibmaks.spring.jfr.bus.SubscribeTo;
import io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer.*;
import io.github.sibmaks.spring.jfr.dto.protobuf.processing.Event;
import io.github.sibmaks.spring.jfr.event.core.converter.ArrayConverter;
import io.github.sibmaks.spring.jfr.event.recording.kafka.consumer.KafkaConsumerCreatedEvent;
import io.github.sibmaks.spring.jfr.event.recording.kafka.consumer.commit.KafkaConsumerCommitEvent;
import io.github.sibmaks.spring.jfr.event.recording.kafka.consumer.commit.KafkaConsumerCommitFailedEvent;
import io.github.sibmaks.spring.jfr.event.recording.kafka.consumer.commit.KafkaConsumerCommitedEvent;
import io.github.sibmaks.spring.jfr.event.recording.kafka.consumer.topic.KafkaConsumerTopicsSubscribedEvent;
import io.github.sibmaks.spring.jfr.event.recording.kafka.consumer.topic.partition.KafkaConsumerPartitionAssignedEvent;
import io.github.sibmaks.spring.jfr.event.recording.kafka.consumer.topic.partition.KafkaConsumerPartitionLostEvent;
import io.github.sibmaks.spring.jfr.event.recording.kafka.consumer.topic.partition.KafkaConsumerPartitionRevokedEvent;
import io.github.sibmaks.spring.jfr.service.StringConstantRegistry;
import lombok.extern.slf4j.Slf4j;
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
    private final Map<String, Event> commits;
    private final StringConstantRegistry stringConstantRegistry;

    public KafkaConsumersReportCreator(StringConstantRegistry stringConstantRegistry) {
        this.contexts = new HashMap<>();
        this.consumerPartitionOffsets = new HashMap<>();
        this.consumers = new HashMap<>();
        this.consumersStats = new HashMap<>();
        this.commits = new HashMap<>();
        this.stringConstantRegistry = stringConstantRegistry;
    }

    private static Map<Map.Entry<String, Integer>, String> getOffsetsAsMap(String offsets) {
        var offsetsMap = new HashMap<Map.Entry<String, Integer>, String>();
        var offsetsAsArray = ArrayConverter.convert(offsets);
        for (int i = 0; i < offsetsAsArray.length; i += 2) {
            var key = offsetsAsArray[i];
            var keyOffset = key.lastIndexOf('-');
            var topic = key.substring(0, keyOffset);
            var partition = Integer.parseInt(key.substring(keyOffset + 1));
            var value = offsetsAsArray[i + 1];
            offsetsMap.put(Map.entry(topic, partition), value);
        }
        return offsetsMap;
    }

    @SubscribeTo(KafkaConsumerCreatedEvent.class)
    public void onKafkaConsumerCreated(Event event) {
        try {
            var contextId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("contextId"));

            var consumerFactoryId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("consumerFactory"));

            var consumerId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("consumerId"));

            var consumerGroup = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("consumerGroup"));

            var bootstrapServers = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("bootstrapServers"));

            var topics = Arrays.stream(ArrayConverter.convert(event.getStringFieldsOrDefault("topics", null)))
                    .map(stringConstantRegistry::getOrRegister)
                    .toList();

            var kafkaConsumer = consumers.computeIfAbsent(consumerId, it -> KafkaConsumer.newBuilder());

            kafkaConsumer
                    .setConsumerFactory(consumerFactoryId)
                    .setConsumerId(consumerId)
                    .setBootstrapServers(bootstrapServers)
                    .setConsumerGroup(consumerGroup)
                    .addAllTopics(topics);

            var contextConsumers = contexts.computeIfAbsent(contextId, k -> new HashMap<>());
            contextConsumers.put(consumerId, kafkaConsumer);
        } catch (Exception e) {
            log.error("KafkaConsumerCreatedRecordedEvent processing error", e);
        }
    }

    @SubscribeTo(KafkaConsumerTopicsSubscribedEvent.class)
    public void onKafkaConsumerTopicsSubscribed(Event event) {
        try {
            var consumerId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("consumerId"));
            var consumerBuilder = consumers.computeIfAbsent(consumerId, it -> KafkaConsumer.newBuilder());
            var topics = new HashSet<>(consumerBuilder.getTopicsList());
            topics.addAll(
                    Arrays.stream(ArrayConverter.convert(event.getStringFieldsOrThrow("topics")))
                            .map(stringConstantRegistry::getOrRegister)
                            .toList()
            );
            consumerBuilder.clearTopics();
            consumerBuilder.addAllTopics(topics);
        } catch (Exception e) {
            log.error("KafkaConsumerTopicsSubscribedRecordedEvent processing error", e);
        }
    }

    @SubscribeTo(KafkaConsumerCommitEvent.class)
    public void onKafkaConsumerCommit(Event event) {
        try {
            commits.put(
                    event.getStringFieldsOrThrow("commitId"),
                    event
            );
            var consumerId = stringConstantRegistry.getOrRegister(event.getStringFieldsOrThrow("consumerId"));
            var stats = consumersStats.computeIfAbsent(consumerId, it -> KafkaConsumerStats.newBuilder());
            stats
                    .setCommits(stats.getCommits() + 1);
        } catch (Exception e) {
            log.error("KafkaConsumerCommitRecordedEvent processing error", e);
        }
    }

    @SubscribeTo(KafkaConsumerCommitedEvent.class)
    public void onKafkaConsumerCommited(Event event) {
        try {
            var rawCommitId = event.getStringFieldsOrThrow("commitId");
            var commitFact = commits.remove(rawCommitId);
            if (commitFact == null) {
                log.warn("Commit with id {} not found", rawCommitId);
                return;
            }
            var consumerId = stringConstantRegistry.getOrRegister(commitFact.getStringFieldsOrThrow("consumerId"));

            var stats = consumersStats.computeIfAbsent(consumerId, it -> KafkaConsumerStats.newBuilder());
            stats
                    .setCommited(stats.getCommited() + 1)
                    .setLastCommitAt(Math.max(stats.getLastCommitAt(), event.getEndTime()));

            var partitionBuilders = consumerPartitionOffsets.computeIfAbsent(consumerId, it -> new HashMap<>());
            var offsetsAsMap = getOffsetsAsMap(commitFact.getStringFieldsOrDefault("offsets", null));
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

    @SubscribeTo(KafkaConsumerCommitFailedEvent.class)
    public void onKafkaConsumerCommitFailed(Event event) {
        try {
            var rawCommitId = event.getStringFieldsOrThrow("commitId");
            var commitFact = commits.remove(rawCommitId);
            if (commitFact == null) {
                log.warn("Commit with id {} not found", rawCommitId);
                return;
            }
            var consumerId = stringConstantRegistry.getOrRegister(commitFact.getStringFieldsOrThrow("consumerId"));
            var stats = consumersStats.computeIfAbsent(consumerId, it -> KafkaConsumerStats.newBuilder());
            stats
                    .setCommitFailed(stats.getCommitFailed() + 1);
        } catch (Exception e) {
            log.error("KafkaConsumerCommitFailedRecordedEvent processing error", e);
        }
    }

    @SubscribeTo(KafkaConsumerPartitionAssignedEvent.class)
    public void onKafkaConsumerPartitionAssigned(Event event) {
        onPartitionEvent(
                event,
                KafkaConsumerPartitionEventType.ASSIGNED
        );
    }

    @SubscribeTo(KafkaConsumerPartitionRevokedEvent.class)
    public void onKafkaConsumerPartitionRevoked(Event event) {
        onPartitionEvent(
                event,
                KafkaConsumerPartitionEventType.REVOKED
        );
    }

    @SubscribeTo(KafkaConsumerPartitionLostEvent.class)
    public void onKafkaConsumerPartitionLost(Event event) {
        onPartitionEvent(
                event,
                KafkaConsumerPartitionEventType.LOST
        );
    }

    private void onPartitionEvent(
            Event event,
            KafkaConsumerPartitionEventType type
    ) {
        var rawConsumerId = event.getStringFieldsOrThrow("consumerId");
        var partitions = ArrayConverter.convert(event.getStringFieldsOrDefault("partitions", null));
        var startTime = event.getStartTime();
        try {
            var consumerId = stringConstantRegistry.getOrRegister(rawConsumerId);
            var consumer = consumers.computeIfAbsent(consumerId, it -> KafkaConsumer.newBuilder());
            var partitionEvent = KafkaConsumerPartitionEvent.newBuilder()
                    .setEventType(type)
                    .addAllPartitions(
                            Arrays.stream(partitions)
                                    .map(stringConstantRegistry::getOrRegister)
                                    .toList()
                    )
                    .setAt(startTime)
                    .build();

            consumer.addPartitionsEvents(partitionEvent);
        } catch (Exception e) {
            log.error("Partition event processing error", e);
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
