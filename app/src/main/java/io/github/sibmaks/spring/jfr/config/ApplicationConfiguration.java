package io.github.sibmaks.spring.jfr.config;

import io.github.sibmaks.spring.jfr.event.reading.core.RecordedEventProxyFactory;
import io.github.sibmaks.spring.jfr.event.reading.core.recorded.RecordedEventFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author sibmaks
 * @since 0.0.2
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {

    @Bean
    public RecordedEventProxyFactory recordedEventProxyFactory() {
        return new RecordedEventProxyFactory();
    }

    @Bean
    public RecordedEventFactory recordedEventFactory(RecordedEventProxyFactory factory) {
        return new RecordedEventFactory(factory);
    }
}
