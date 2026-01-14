package com.onclass.report.mongo;

import com.onclass.report.mongo.document.BootcampReportDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface BootcampReportRepository extends ReactiveMongoRepository<BootcampReportDocument, String>, ReactiveQueryByExampleExecutor<BootcampReportDocument> {

}

