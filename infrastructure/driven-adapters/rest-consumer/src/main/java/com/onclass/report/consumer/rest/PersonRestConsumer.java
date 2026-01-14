package com.onclass.report.consumer.rest;

import com.onclass.report.consumer.dto.response.CapabilityListResponseDto;
import com.onclass.report.consumer.dto.response.PersonResponseWrapperDto;
import com.onclass.report.model.bootcampreport.StudentReport;
import com.onclass.report.model.enums.ExceptionMessages;
import com.onclass.report.model.exceptions.PersonMicroserviceException;
import com.onclass.report.model.port.consumer.PersonConsumerPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PersonRestConsumer implements PersonConsumerPort {
    private static final String GET_PERSON_URL = "/person/api/v1/persons/{personId}";

    private final WebClient personWebClient;

    public PersonRestConsumer(@Qualifier("personWebClient") WebClient personWebClient) {
        this.personWebClient = personWebClient;
    }

    @Override
    public Mono<StudentReport> getPersonById(Long personId) {
        return personWebClient.get()
                .uri(GET_PERSON_URL, personId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new PersonMicroserviceException(
                                        ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.format(body)
                                ))
                        )
                )
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(PersonResponseWrapperDto.class)
                .map(PersonResponseWrapperDto::data)
                .doOnError(e -> log.error("Error calling Person Microservice for ID: {}", personId, e));
    }
}
