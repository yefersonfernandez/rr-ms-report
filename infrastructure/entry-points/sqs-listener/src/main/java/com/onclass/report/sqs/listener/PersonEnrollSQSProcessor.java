package com.onclass.report.sqs.listener;

import com.onclass.report.sqs.listener.dto.PersonEnrollDto;
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
@Service("personEnrollQueueProcessor")
@RequiredArgsConstructor
public class PersonEnrollSQSProcessor implements Function<Message, Mono<Void>> {
    private final BootcampReportUseCase bootcampReportUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {
        log.info("Received SQS message to person enroll: {}", message.body());

        return Mono.fromCallable(() -> objectMapper.readValue(message.body(), PersonEnrollDto.class))
                .doOnNext(data -> log.info("Processing BootcampId={} with PersonId={}", data.getBootcampId(), data.getPersonId()))
                .flatMap(data -> bootcampReportUseCase.addPersonToBootcampReport(data.getBootcampId(), data.getPersonId()))
                .doOnError(error -> log.error("Error processing SQS message: {}", message.body(), error))
                .then();
    }
}
