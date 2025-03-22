package io.github.sibmaks.spring.jfr.dto.view.connections;

import lombok.Builder;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record ConnectionException(long type, long message) {
}
