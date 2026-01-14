package com.onclass.report.consumer.dto.response;

import com.onclass.report.model.bootcampreport.CapabilityReport;
import java.util.List;

public record CapabilityListResponseDto(List<CapabilityReport> data) {}
