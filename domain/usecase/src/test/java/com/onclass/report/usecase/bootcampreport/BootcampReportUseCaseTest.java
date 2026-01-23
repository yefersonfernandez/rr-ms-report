package com.onclass.report.usecase.bootcampreport;

import com.onclass.report.enums.ExceptionMessages;
import com.onclass.report.exceptions.NotFoundException;
import com.onclass.report.model.bootcampreport.BootcampCreated;
import com.onclass.report.model.bootcampreport.BootcampReport;
import com.onclass.report.model.bootcampreport.CapabilityReport;
import com.onclass.report.model.bootcampreport.StudentReport;
import com.onclass.report.model.bootcampreport.TechnologyReport;
import com.onclass.report.model.bootcampreport.gateways.BootcampReportRepositoryPort;
import com.onclass.report.port.consumer.CapabilityConsumerPort;
import com.onclass.report.port.consumer.PersonConsumerPort;
import com.onclass.report.port.consumer.TechnologyConsumerPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampReportUseCaseTest {

    @Mock
    private BootcampReportRepositoryPort bootcampReportRepositoryPort;
    @Mock
    private CapabilityConsumerPort capabilityConsumerPort;
    @Mock
    private TechnologyConsumerPort technologyConsumerPort;
    @Mock
    private PersonConsumerPort personConsumerPort;

    @InjectMocks
    private BootcampReportUseCase bootcampReportUseCase;

    @Test
    @DisplayName("Should handle bootcamp creation, aggregate data and save report")
    void handleBootcampCreation_shouldSaveBootcampReport() {
        Long bootcampId = 1L;
        Long capabilityId = 10L;
        BootcampCreated bootcampCreated = BootcampCreated.builder()
                .bootcampId(bootcampId)
                .name("Java Bootcamp")
                .build();

        CapabilityReport capability = CapabilityReport.builder()
                .id(capabilityId)
                .name("Backend")
                .build();

        TechnologyReport tech = TechnologyReport.builder()
                .id(100L)
                .name("Java")
                .build();

        when(capabilityConsumerPort.getCapabilitiesByBootcampId(bootcampId)).thenReturn(Flux.just(capability));
        when(technologyConsumerPort.getTechnologiesByCapabilityId(capabilityId)).thenReturn(Flux.just(tech));

        when(bootcampReportRepositoryPort.save(any(BootcampReport.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Void> result = bootcampReportUseCase.handleBootcampCreation(bootcampCreated);

        StepVerifier.create(result)
                .verifyComplete();

        ArgumentCaptor<BootcampReport> reportCaptor = ArgumentCaptor.forClass(BootcampReport.class);
        verify(bootcampReportRepositoryPort).save(reportCaptor.capture());

        BootcampReport savedReport = reportCaptor.getValue();
        assertThat(savedReport.getBootcampId()).isEqualTo(bootcampId);
    }

    @Test
    @DisplayName("Should retrieve student info and add to bootcamp report")
    void addPersonToBootcampReport_shouldAddStudent() {
        Long bootcampId = 1L;
        Long personId = 2L;
        StudentReport student = StudentReport.builder()
                .id(personId)
                .name("Test User")
                .build();

        when(personConsumerPort.getPersonById(personId)).thenReturn(Mono.just(student));
        when(bootcampReportRepositoryPort.addStudentToReport(bootcampId, student)).thenReturn(Mono.empty());

        Mono<Void> result = bootcampReportUseCase.addPersonToBootcampReport(bootcampId, personId);

        StepVerifier.create(result)
                .verifyComplete();

        verify(bootcampReportRepositoryPort).addStudentToReport(bootcampId, student);
    }

    @Test
    @DisplayName("Should return the most enrolled bootcamp report")
    void getMostSuccessfulBootcamp_shouldReturnBootcampReport() {
        BootcampReport report = BootcampReport.builder()
                .id("mongo-id")
                .bootcampId(1L)
                .build();

        when(bootcampReportRepositoryPort.findMostEnrolledBootcamp()).thenReturn(Mono.just(report));

        StepVerifier.create(bootcampReportUseCase.getMostSuccessfulBootcamp())
                .expectNext(report)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should throw NotFoundException when no bootcamp report is found")
    void getMostSuccessfulBootcamp_shouldThrowNotFoundException() {
        when(bootcampReportRepositoryPort.findMostEnrolledBootcamp()).thenReturn(Mono.empty());

        StepVerifier.create(bootcampReportUseCase.getMostSuccessfulBootcamp())
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException
                        && throwable.getMessage().equals(ExceptionMessages.BOOTCAMP_NOT_FOUND.getMessage()))
                .verify();
    }
}