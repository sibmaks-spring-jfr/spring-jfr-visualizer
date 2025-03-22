package io.github.sibmaks.spring.jfr.dto.view.connections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public final class Connection {
    private final List<ConnectionEvent> events;
    private String id;
    private long duration;
    private boolean hasExceptions;
}
