package io.github.sibmaks.spring.jfr.dto.view.calls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
@Builder
@AllArgsConstructor
public class CallsReport {
    private final List<CallTrace> roots;
}
