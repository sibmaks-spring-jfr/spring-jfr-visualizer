package io.github.sibmaks.spring.jfr.dto.view.calls;

import lombok.Builder;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Builder
public record CallsReport(List<CallTrace> roots) {
}
