package com.onclass.report.sqs.listener.config;

import com.onclass.report.sqs.listener.helper.SQSListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SQSConfigTest {

    @InjectMocks
    private SQSConfig sqsConfig;

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @Mock
    private SQSProperties sqsProperties;

    @Mock
    private Function<Message, Mono<Void>> processor;

    @BeforeEach
    void setUp() {
        lenient().when(sqsProperties.region()).thenReturn("us-east-1");
        lenient().when(sqsProperties.bootcampReportQueueUrl()).thenReturn("http://localhost:4566/queue/bootcamp-report");
        lenient().when(sqsProperties.personEnrollmentQueueUrl()).thenReturn("http://localhost:4566/queue/person-enrollment");
        lenient().when(sqsProperties.waitTimeSeconds()).thenReturn(20);
        lenient().when(sqsProperties.maxNumberOfMessages()).thenReturn(10);
        lenient().when(sqsProperties.numberOfThreads()).thenReturn(1);
    }

    @Test
    @DisplayName("bootcampCreatedQueueListener should create and return a listener")
    void bootcampCreatedQueueListener_shouldReturnListener() {
        SQSListener listener = sqsConfig.bootcampCreatedQueueListener(
                sqsAsyncClient,
                sqsProperties,
                processor
        );

        assertThat(listener).isNotNull();
        verify(sqsProperties).bootcampReportQueueUrl();
    }

    @Test
    @DisplayName("personEnrollQueueListener should create and return a listener")
    void personEnrollQueueListener_shouldReturnListener() {
        SQSListener listener = sqsConfig.personEnrollQueueListener(
                sqsAsyncClient,
                sqsProperties,
                processor
        );

        assertThat(listener).isNotNull();
        verify(sqsProperties).personEnrollmentQueueUrl();
    }
}