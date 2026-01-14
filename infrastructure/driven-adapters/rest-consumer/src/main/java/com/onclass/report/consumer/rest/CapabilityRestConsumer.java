package com.onclass.report.consumer.rest;

import com.onclass.report.consumer.dto.response.CapabilityListResponseDto;
import com.onclass.report.model.bootcampreport.CapabilityReport;
import com.onclass.report.model.enums.ExceptionMessages;
import com.onclass.report.model.exceptions.CapabilityMicroserviceException;
import com.onclass.report.model.port.consumer.CapabilityConsumerPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CapabilityRestConsumer implements CapabilityConsumerPort {
    private static final String GET_CAPABILITIES_URL = "/capability/api/v1/botcamps/{bootcampId}/capabilities";

    private final WebClient capabilityWebClient;

    public CapabilityRestConsumer(@Qualifier("capabilityWebClient") WebClient capabilityWebClient) {
        this.capabilityWebClient = capabilityWebClient;
    }


    @Override
    public Flux<CapabilityReport> getCapabilitiesByBootcampId(Long bootcampId) {
        return capabilityWebClient.get()
                .uri(GET_CAPABILITIES_URL, bootcampId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new CapabilityMicroserviceException(
                                        ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.format(body)
                                ))
                        )
                )
                .bodyToMono(CapabilityListResponseDto.class)
                .flatMapMany(response -> Flux.fromIterable(response.data() != null ? response.data() : List.of()));
    }
}
