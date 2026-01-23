package com.onclass.report.consumer.rest;

import com.onclass.report.enums.ExceptionMessages;
import com.onclass.report.exceptions.CapabilityMicroserviceException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.io.IOException;


class CapabilityRestConsumerTest {
    private static CapabilityRestConsumer consumer;
    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        consumer = new CapabilityRestConsumer(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("getCapabilitiesByBootcampId returns capabilities on success")
    void getCapabilitiesByBootcampId_success() {
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"code\":200,\"data\":[{\"id\":1,\"name\":\"Java\"},{\"id\":2,\"name\":\"Spring Boot\"}]}")
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(consumer.getCapabilitiesByBootcampId(123L))
                .expectNextMatches(c -> c.getId().equals(1L) || c.getName().equals("Java"))
                .expectNextMatches(c -> c.getId().equals(2L) || c.getName().equals("Spring Boot"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getCapabilitiesByBootcampId returns empty Flux when no data")
    void getCapabilitiesByBootcampId_empty() {
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"code\":200,\"data\":[]}")
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(consumer.getCapabilitiesByBootcampId(999L))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("getCapabilitiesByBootcampId returns CapabilityMicroserviceException on server error")
    void getCapabilitiesByBootcampId_serverError() {
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("Server error")
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(consumer.getCapabilitiesByBootcampId(123L))
                .expectErrorSatisfies(e -> {
                    assert e instanceof CapabilityMicroserviceException;
                    assert e.getMessage().contains(ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.getMessage());
                })
                .verify();
    }
}

