package com.onclass.report.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Report Microservice",
                description = "Manages Reports."
        )
)
public class SwaggerConfig {
}
