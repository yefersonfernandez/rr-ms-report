package com.onclass.report.api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StudentReportDto {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
