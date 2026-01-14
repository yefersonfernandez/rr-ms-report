package com.onclass.report.consumer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "adapter.restconsumer")
public class RestConsumerProperties {
    private String technologyUrl;
    private String capabilityUrl;
    private String personUrl;
    private int timeout;
}
