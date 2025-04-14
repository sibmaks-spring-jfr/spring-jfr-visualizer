package io.github.sibmaks.spring.jfr.bus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubscribeTo {
    /**
     * Class as topic names
     *
     * @return topic names
     */
    Class<?>[] value();
}
