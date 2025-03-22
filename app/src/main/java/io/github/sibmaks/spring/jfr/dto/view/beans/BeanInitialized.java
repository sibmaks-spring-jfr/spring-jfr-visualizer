package io.github.sibmaks.spring.jfr.dto.view.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@Builder
@AllArgsConstructor
public class BeanInitialized {
    private int contextId;
    private int beanName;
    private Long preInitializedAt;
    private Long postInitializedAt;
    private double duration;
}
