package com.onclass.report.consumer.dto.response;


import com.onclass.report.model.bootcampreport.TechnologyReport;

import java.util.List;

public record TechnologyListResponseDto(List<TechnologyReport> data) {}
