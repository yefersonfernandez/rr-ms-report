package com.onclass.report.api.mapper;

import com.onclass.report.api.dto.response.BootcampReportDto;
import com.onclass.report.model.bootcampreport.BootcampReport;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ReportMapper {
    BootcampReportDto toBootcampReportDto(BootcampReport bootcampReport);
}
