package com.onclass.report.api.bootcampreport;

import com.onclass.report.api.config.ReportPath;
import com.onclass.report.api.openapi.ReportOpenApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

@Configuration
@RequiredArgsConstructor
public class ReportRouterRest {
    private final ReportPath reportPath;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ReportHandler handler) {
        return route()
                .GET(reportPath.getBootcampTopEnrolled(), handler::listenGetMostSuccessful, ReportOpenApi::getMostSuccessful)
                .build();
    }
}
