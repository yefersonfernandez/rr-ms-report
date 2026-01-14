package com.onclass.report.api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapabilityReportDto {
    private Long id;
    private String name;
    private List<TechnologyReportDto> technologies;
}
