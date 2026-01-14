package com.onclass.report.sqs.listener.config;

import com.onclass.report.sqs.listener.helper.SQSListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Configuration
public class SQSConfig {

    @Bean
    public SqsAsyncClient configSqs(SQSProperties properties) {
        return SqsAsyncClient.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(getProviderChain())
                .build();
    }

    @Bean
    public SQSListener bootcampCreatedQueueListener(
            SqsAsyncClient client,
            SQSProperties properties,
            @Qualifier("bootcampCreatedQueueProcessor")
            Function<Message, Mono<Void>> fn
    ) {
        return createListener(client, properties, properties.bootcampReportQueueUrl(), fn);
    }

    @Bean
    public SQSListener personEnrollQueueListener(
            SqsAsyncClient client,
            SQSProperties properties,
            @Qualifier("personEnrollQueueProcessor")
            Function<Message, Mono<Void>> fn
    ) {
        return createListener(client, properties, properties.personEnrollmentQueueUrl(), fn);
    }

    private SQSListener createListener(
            SqsAsyncClient client,
            SQSProperties properties,
            String queueUrl,
            Function<Message, Mono<Void>> processor
    ) {
        return SQSListener.builder()
                .client(client)
                .properties(properties)
                .queueUrl(queueUrl)
                .processor(processor)
                .build()
                .start();
    }

    private AwsCredentialsProviderChain getProviderChain() {
        return AwsCredentialsProviderChain.builder()
                .addCredentialsProvider(SystemPropertyCredentialsProvider.create())
                .addCredentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .addCredentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }
}