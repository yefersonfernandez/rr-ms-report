package com.onclass.report.model.bootcampreport.gateways;

import com.onclass.report.model.bootcampreport.BootcampReport;
import com.onclass.report.model.bootcampreport.StudentReport;
import reactor.core.publisher.Mono;

public interface BootcampReportRepositoryPort {
    Mono<BootcampReport> save(BootcampReport bootcampReport);
    Mono<Void> addStudentToReport(Long bootcampId, StudentReport student);
    Mono<BootcampReport> findMostEnrolledBootcamp();
}
