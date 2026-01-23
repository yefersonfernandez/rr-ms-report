package com.onclass.report.mongo;

import com.mongodb.client.result.UpdateResult;
import com.onclass.report.model.bootcampreport.BootcampReport;
import com.onclass.report.model.bootcampreport.StudentReport;
import com.onclass.report.mongo.document.BootcampReportDocument;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampReportRepositoryAdapterTest {

    @Mock
    private BootcampReportRepository repository;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private ReactiveMongoTemplate mongoTemplate;

    @InjectMocks
    private BootcampReportRepositoryAdapter adapter;

    @Test
    @DisplayName("addStudentToReport should update students and enrolledStudentCount")
    void addStudentToReport_shouldUpdate() {
        Long bootcampId = 1L;
        StudentReport student = StudentReport.builder().id(100L).name("Test Student").build();

        UpdateResult updateResult = mock(UpdateResult.class);

        when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(BootcampReportDocument.class)))
                .thenReturn(Mono.just(updateResult));

        Mono<Void> result = adapter.addStudentToReport(bootcampId, student);

        StepVerifier.create(result).verifyComplete();

        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);

        verify(mongoTemplate).updateFirst(queryCaptor.capture(), updateCaptor.capture(), eq(BootcampReportDocument.class));

        Document queryObject = queryCaptor.getValue().getQueryObject();
        assertThat(queryObject).containsEntry("bootcampId", bootcampId);
        assertThat(queryObject.toString()).contains("students.id", "$ne");

        Document updateObject = updateCaptor.getValue().getUpdateObject();
        assertThat(updateObject).containsKey("$push").containsKey("$inc");

        Document pushObj = (Document) updateObject.get("$push");
        assertThat(pushObj).containsKey("students");

        Document incObj = (Document) updateObject.get("$inc");
        assertThat(incObj).containsEntry("enrolledStudentCount", 1);
    }

    @Test
    @DisplayName("findMostEnrolledBootcamp should return mapped BootcampReport")
    void findMostEnrolledBootcamp_shouldReturnMapped() {
        BootcampReportDocument doc = new BootcampReportDocument();
        doc.setId("mongo-id");
        doc.setBootcampId(50L);

        BootcampReport entity = BootcampReport.builder().id("mongo-id").bootcampId(50L).build();

        when(mongoTemplate.findOne(any(Query.class), eq(BootcampReportDocument.class)))
                .thenReturn(Mono.just(doc));
        when(mapper.map(doc, BootcampReport.class)).thenReturn(entity);

        Mono<BootcampReport> result = adapter.findMostEnrolledBootcamp();

        StepVerifier.create(result)
                .expectNextMatches(report -> report.getBootcampId().equals(50L))
                .verifyComplete();

        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        verify(mongoTemplate).findOne(queryCaptor.capture(), eq(BootcampReportDocument.class));

        Query capturedQuery = queryCaptor.getValue();
        Document sortObject = capturedQuery.getSortObject();

        assertThat(sortObject).containsEntry("enrolledStudentCount", -1);
        assertThat(capturedQuery.getLimit()).isEqualTo(1);
    }
}