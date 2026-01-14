package com.onclass.report.model.port.consumer;

import com.onclass.report.model.bootcampreport.StudentReport;
import reactor.core.publisher.Mono;

public interface PersonConsumerPort {
    Mono<StudentReport> getPersonById(Long personId);
}
