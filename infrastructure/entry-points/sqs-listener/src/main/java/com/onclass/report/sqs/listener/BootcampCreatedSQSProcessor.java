package com.onclass.report.sqs.listener;

import com.onclass.report.sqs.listener.dto.BootcampCreatedDto;
import com.onclass.report.sqs.listener.mapper.BootcampReportMapper;
import com.onclass.report.usecase.bootcampreport.BootcampReportUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;
import tools.jackson.databind.ObjectMapper;
import java.util.function.Function;

@Slf4j
@Service("bootcampCreatedQueueProcessor")
@RequiredArgsConstructor
public class BootcampCreatedSQSProcessor implements Function<Message, Mono<Void>> {
    private final BootcampReportUseCase bootcampReportUseCase;
    private final BootcampReportMapper bootcampCreatedMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Received SQS message to bootcamp created: {}", message.body());

        return Mono.fromCallable(() -> objectMapper.readValue(message.body(), BootcampCreatedDto.class))
                .map(bootcampCreatedMapper::toModel)
                .doOnNext(bootcampCreated -> log.info("Processing BootcampId={} with name={}", bootcampCreated.getBootcampId(), bootcampCreated.getName()))
                .flatMap(bootcampReportUseCase::handleBootcampCreation)
                .doOnSuccess(unused -> log.info("Successfully processed SQS message: {}", message.body()))
                .doOnError(error -> log.error("Error processing SQS message: {}", message.body(), error))
                .then();
    }
}
