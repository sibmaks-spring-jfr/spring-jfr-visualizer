package io.github.sibmaks.spring.jfr.dto.view.common;

import lombok.Builder;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record CommonDto(
        Map<Long, String> stringConstants
) {
}
