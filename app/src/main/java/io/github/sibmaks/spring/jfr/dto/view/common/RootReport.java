package io.github.sibmaks.spring.jfr.dto.view.common;


import io.github.sibmaks.spring.jfr.dto.protobuf.kafka.consumer.KafkaConsumersReport;
import io.github.sibmaks.spring.jfr.dto.view.beans.BeansReport;
import io.github.sibmaks.spring.jfr.dto.view.calls.CallsReport;
import io.github.sibmaks.spring.jfr.dto.view.connections.ConnectionsReport;
import lombok.Builder;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record RootReport(
        CommonDto common,
        BeansReport beans,
        CallsReport calls,
        ConnectionsReport connections,
        KafkaConsumersReport kafkaConsumers
) {
}
