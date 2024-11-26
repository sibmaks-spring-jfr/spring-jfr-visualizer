package io.github.sibmaks.spring.jfr.dto.view;

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
    private String contextId;
    private String beanName;
    private Long preInitializedAt;
    private Long postInitializedAt;
    private double duration;
}
