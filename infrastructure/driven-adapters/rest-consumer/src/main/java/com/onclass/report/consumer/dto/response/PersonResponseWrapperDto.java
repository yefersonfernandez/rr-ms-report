package com.onclass.report.consumer.dto.response;

import com.onclass.report.model.bootcampreport.StudentReport;

public record PersonResponseWrapperDto(
        StudentReport data
) {}