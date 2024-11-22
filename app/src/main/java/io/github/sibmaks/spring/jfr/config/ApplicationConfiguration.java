package io.github.sibmaks.spring.jfr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.sibmaks.spring.jfr.event.core.RecordedEventProxyFactory;
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
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public RecordedEventProxyFactory recordedEventProxyFactory() {
        return new RecordedEventProxyFactory();
    }
}
