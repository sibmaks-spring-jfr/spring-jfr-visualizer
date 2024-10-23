package io.github.sibmaks.spring.jfr;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Getter
@AllArgsConstructor
public class BeanInitialized {
    private String beanName;
    private double duration;
}
