package io.github.sibmaks.spring.jfr.report.connections.dto;

import io.github.sibmaks.spring.jfr.dto.view.connections.Connection;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Getter
@Setter
public final class ContextDto {
    private final Map<String, PoolDto> pools;

    public ContextDto() {
        this.pools = new HashMap<>();
    }

    public PoolDto get(String poolId) {
        return pools.computeIfAbsent(poolId, it -> new PoolDto());
    }

    public Map<String, List<Connection>> toPoolMap() {
        var poolMap = new HashMap<String, List<Connection>>();

        for (var entry : this.pools.entrySet()) {
            var key = entry.getKey();
            var context = entry.getValue();
            var connections = context.toConnections();
            poolMap.put(key, connections);
        }

        return poolMap;
    }
}
