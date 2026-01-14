package com.onclass.report.sqs.listener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEnrollDto {
    private Long bootcampId;
    private Long personId;
}
