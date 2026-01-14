package com.onclass.report.model.port.consumer;

import com.onclass.report.model.bootcampreport.CapabilityReport;
import reactor.core.publisher.Flux;

public interface CapabilityConsumerPort {
    Flux<CapabilityReport> getCapabilitiesByBootcampId(Long bootcampId);
}
