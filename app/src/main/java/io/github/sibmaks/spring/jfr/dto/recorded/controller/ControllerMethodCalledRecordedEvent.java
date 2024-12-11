package io.github.sibmaks.spring.jfr.dto.recorded.controller;

import io.github.sibmaks.spring.jfr.dto.recorded.RecordedData;
import io.github.sibmaks.spring.jfr.event.api.controller.ControllerMethodCalledFact;

/**
 * @author sibmaks
 * @since 0.0.2
 */
public interface ControllerMethodCalledRecordedEvent extends ControllerMethodCalledFact, RecordedData {
}
