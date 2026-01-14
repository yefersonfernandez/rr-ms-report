package com.onclass.report.api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TechnologyReportDto {
    private Long id;
    private String name;
}
