package com.onclass.report.mongo;

import com.onclass.report.model.bootcampreport.BootcampReport;
import com.onclass.report.model.bootcampreport.StudentReport;
import com.onclass.report.model.bootcampreport.gateways.BootcampReportRepositoryPort;
import com.onclass.report.mongo.document.BootcampReportDocument;
import com.onclass.report.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


@Repository
public class BootcampReportRepositoryAdapter extends AdapterOperations<
        BootcampReport,
        BootcampReportDocument,
        String,
        BootcampReportRepository
    > implements BootcampReportRepositoryPort {

    private final ReactiveMongoTemplate mongoTemplate;

    public BootcampReportRepositoryAdapter(BootcampReportRepository repository,
                                           ObjectMapper mapper,
                                           ReactiveMongoTemplate mongoTemplate) {
        super(repository, mapper, d -> mapper.map(d, BootcampReport.class));
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Void> addStudentToReport(Long bootcampId, StudentReport student) {
        var query = new Query(Criteria.where("bootcampId").is(bootcampId)
                .and("students.id").ne(student.getId()));

        var update = new Update()
                .push("students", student)
                .inc("enrolledStudentCount", 1);

        return mongoTemplate.updateFirst(query, update, BootcampReportDocument.class)
                .then();
    }

    @Override
    public Mono<BootcampReport> findMostEnrolledBootcamp() {
        var query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "enrolledStudentCount"))
                .limit(1);

        return mongoTemplate.findOne(query, BootcampReportDocument.class)
                .map(super::toEntity);
    }
}
