package com.onclass.report.model.port.consumer;

import com.onclass.report.model.bootcampreport.TechnologyReport;
import reactor.core.publisher.Flux;

public interface TechnologyConsumerPort {
    Flux<TechnologyReport> getTechnologiesByCapabilityId(Long capabilityId);
}
