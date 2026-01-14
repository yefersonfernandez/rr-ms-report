package com.onclass.report.api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampReportDto {
    private String id;
    private Long bootcampId;
    private String name;
    private String description;
    private Integer capabilityCount;
    private Integer technologyCount;
    private Integer enrolledStudentCount;
    private List<CapabilityReportDto> capabilities;
    private List<StudentReportDto> students;
}
