package com.onclass.report.api.bootcampreport;

import com.onclass.report.api.config.ReportPath;
import com.onclass.report.api.dto.response.BootcampReportDto;
import com.onclass.report.api.mapper.ReportMapper;
import com.onclass.report.usecase.bootcampreport.BootcampReportUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;

@TestPropertySource(properties = {"routes.paths.bootcamp-top-enrolled=/report/api/v1/bootcamp/top-enrolled"})
@ContextConfiguration(classes = {ReportRouterRest.class, ReportHandler.class, ReportPath.class})
@WebFluxTest
class ReportRouterRestTest {
    private static final String BOOTCAMP_TOP_ENROLLED_PATH = "/report/api/v1/bootcamp/top-enrolled";
    private static final String BOOTCAMP_NAME = "Java Bootcamp";
    private static final int ENROLLED = 100;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReportPath reportPath;

    @MockitoBean
    private BootcampReportUseCase bootcampReportUseCase;
    @MockitoBean
    private ReportMapper reportMapper;

    private BootcampReportDto bootcampReportDto;

    @BeforeEach
    void setUp() {
        bootcampReportDto = BootcampReportDto.builder()
                .id("1")
                .bootcampId(1L)
                .name(BOOTCAMP_NAME)
                .description("Java Bootcamp description")
                .capabilityCount(2)
                .technologyCount(3)
                .enrolledStudentCount(ENROLLED)
                .capabilities(List.of())
                .students(List.of())
                .build();
    }

    @Test
    @DisplayName("Should load path property from ReportPath")
    void shouldLoadBootcampTopEnrolledPathProperty() {
        Assertions.assertThat(reportPath.getBootcampTopEnrolled()).isEqualTo(BOOTCAMP_TOP_ENROLLED_PATH);
    }

    @Test
    @DisplayName("GET /bootcamp/top-enrolled - listenGetMostSuccessful: should return 200 when bootcamp is found")
    void get_mostSuccessful_shouldReturnOk() {
        var domainObj = com.onclass.report.model.bootcampreport.BootcampReport.builder()
                .id("1")
                .bootcampId(1L)
                .name(BOOTCAMP_NAME)
                .description("Java Bootcamp description")
                .capabilityCount(2)
                .technologyCount(3)
                .enrolledStudentCount(ENROLLED)
                .capabilities(List.of())
                .students(List.of())
                .build();
        when(bootcampReportUseCase.getMostSuccessfulBootcamp()).thenReturn(Mono.just(domainObj));
        when(reportMapper.toBootcampReportDto(domainObj)).thenReturn(bootcampReportDto);

        webTestClient.get()
                .uri(BOOTCAMP_TOP_ENROLLED_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo("1")
                .jsonPath("$.data.bootcampId").isEqualTo(1)
                .jsonPath("$.data.name").isEqualTo(BOOTCAMP_NAME)
                .jsonPath("$.data.description").isEqualTo("Java Bootcamp description")
                .jsonPath("$.data.capabilityCount").isEqualTo(2)
                .jsonPath("$.data.technologyCount").isEqualTo(3)
                .jsonPath("$.data.enrolledStudentCount").isEqualTo(ENROLLED)
                .jsonPath("$.data.capabilities").isArray()
                .jsonPath("$.data.students").isArray();
    }
}
