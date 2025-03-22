package io.github.sibmaks.spring.jfr.dto.view.connections;

import lombok.Builder;

import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record ConnectionsReport(Map<Long, Map<Long, List<Connection>>> contexts) {
}
