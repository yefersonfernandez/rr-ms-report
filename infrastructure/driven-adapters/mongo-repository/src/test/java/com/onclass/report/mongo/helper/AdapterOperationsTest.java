package com.onclass.report.mongo.helper;

import com.onclass.report.model.bootcampreport.BootcampReport;
import com.onclass.report.mongo.BootcampReportRepository;
import com.onclass.report.mongo.BootcampReportRepositoryAdapter;
import com.onclass.report.mongo.document.BootcampReportDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdapterOperationsTest {

    @Mock
    private BootcampReportRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ReactiveMongoTemplate mongoTemplate;

    private BootcampReportRepositoryAdapter adapter;

    private BootcampReport entity;
    private BootcampReportDocument document;

    @BeforeEach
    void setUp() {
        adapter = new BootcampReportRepositoryAdapter(repository, objectMapper, mongoTemplate);

        entity = BootcampReport.builder().id("1").bootcampId(10L).build();
        document = new BootcampReportDocument();
        document.setId("1");
        document.setBootcampId(10L);

        lenient().when(objectMapper.map(entity, BootcampReportDocument.class)).thenReturn(document);
        lenient().when(objectMapper.map(document, BootcampReport.class)).thenReturn(entity);
    }

    @Test
    void testSave() {
        when(repository.save(document)).thenReturn(Mono.just(document));

        StepVerifier.create(adapter.save(entity))
                .expectNext(entity)
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testSaveAll() {
        when(repository.saveAll(any(Flux.class))).thenReturn(Flux.just(document));

        StepVerifier.create(adapter.saveAll(Flux.just(entity)))
                .expectNext(entity)
                .verifyComplete();
    }

    @Test
    void testFindById() {
        when(repository.findById("1")).thenReturn(Mono.just(document));

        StepVerifier.create(adapter.findById("1"))
                .expectNext(entity)
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testFindByExample() {
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(document));

        StepVerifier.create(adapter.findByExample(entity))
                .expectNext(entity)
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Flux.just(document));

        StepVerifier.create(adapter.findAll())
                .expectNext(entity)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        when(repository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteById("1"))
                .verifyComplete();
    }
}