package com.onclass.report.consumer.rest;

import com.onclass.report.enums.ExceptionMessages;
import com.onclass.report.exceptions.PersonMicroserviceException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class PersonRestConsumerTest {
    private static PersonRestConsumer consumer;
    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        WebClient webClient = WebClient.builder()
                .baseUrl(mockBackEnd.url("/").toString())
                .build();

        consumer = new PersonRestConsumer(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Successful person query returns mapped StudentReport")
    void getPersonById_success() {
        String responseBody = """
            {
                "code": 200,
                "data": {
                    "id": 1,
                    "name": "John Doe",
                    "lastname": "Smith",
                    "email": "john@email.com"
                },
                "message": "Success"
            }
            """;

        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setBody(responseBody)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        StepVerifier.create(consumer.getPersonById(1L))
                .expectNextMatches(student -> student.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Person not found returns empty Mono")
    void getPersonById_notFound() {
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        StepVerifier.create(consumer.getPersonById(999L))
                .verifyComplete();
    }

    @Test
    @DisplayName("Internal server error returns PersonMicroserviceException")
    void getPersonById_serverError() {
        String errorBody = "Critical Failure";

        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody(errorBody)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        StepVerifier.create(consumer.getPersonById(1L))
                .expectErrorSatisfies(e -> {
                    assertThat(e)
                            .isInstanceOf(PersonMicroserviceException.class)
                            .hasMessageContaining(ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.format(errorBody));
                })
                .verify();
    }
}