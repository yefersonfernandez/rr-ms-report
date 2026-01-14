package com.onclass.report.model.bootcampreport;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StudentReport {
    private Long id;
    private String name;
    private String email;
    private Integer age;
}
