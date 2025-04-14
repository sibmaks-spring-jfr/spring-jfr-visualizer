package io.github.sibmaks.spring.jfr.bus;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventHandlerRegistrar implements BeanPostProcessor {
    private final InMemoryEventBus bus;
    private final Map<String, List<Method>> typeCache = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        var methods = new ArrayList<Method>();
        for (var method : bean.getClass().getMethods()) {
            if (!method.isAnnotationPresent(SubscribeTo.class)) {
                continue;
            }
            if (method.getParameterCount() != 1) {
                throw new IllegalArgumentException("Method " + method.getName() + " should have exactly one parameter");
            }
            methods.add(method);
        }
        if (!methods.isEmpty()) {
            typeCache.put(beanName, methods);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var methods = typeCache.get(beanName);
        if (methods == null) {
            return bean;
        }
        for (var method : methods) {
            var subscribeTo = method.getAnnotation(SubscribeTo.class);
            for (var topic : subscribeTo.value()) {
                bus.subscribe(topic.getName(), event -> {
                    try {
                        method.invoke(bean, event);
                    } catch (Exception e) {
                        throw new RuntimeException("Event consumer calling error", e);
                    }
                });
            }
            for (var topic : subscribeTo.topics()) {
                bus.subscribe(topic, event -> {
                    try {
                        method.invoke(bean, event);
                    } catch (Exception e) {
                        throw new RuntimeException("Event consumer calling error", e);
                    }
                });
            }
        }
        return bean;
    }
}
