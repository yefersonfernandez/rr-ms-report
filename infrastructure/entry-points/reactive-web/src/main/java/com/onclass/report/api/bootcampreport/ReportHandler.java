package com.onclass.report.api.bootcampreport;

import com.onclass.report.api.mapper.ReportMapper;
import com.onclass.report.enums.ExceptionStatusCode;
import com.onclass.report.usecase.bootcampreport.BootcampReportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.onclass.report.api.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Component
@RequiredArgsConstructor
public class ReportHandler {
    private final BootcampReportUseCase bootcampReportUseCase;
    private final ReportMapper reportMapper;

    public Mono<ServerResponse> listenGetMostSuccessful(ServerRequest request) {
        return bootcampReportUseCase.getMostSuccessfulBootcamp()
                .map(reportMapper::toBootcampReportDto)
                .flatMap(dto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.OK.status(), dto))
                );
    }
}
