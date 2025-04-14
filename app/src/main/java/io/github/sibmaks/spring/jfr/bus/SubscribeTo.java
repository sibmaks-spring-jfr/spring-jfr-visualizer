package io.github.sibmaks.spring.jfr.bus;

import jdk.jfr.Event;

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
    Class<? extends Event>[] value();

    /**
     * String as topic names
     *
     * @return topic names
     */
    String[] topics() default {};
}
