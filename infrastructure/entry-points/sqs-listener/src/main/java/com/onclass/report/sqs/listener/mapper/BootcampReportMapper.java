package com.onclass.report.sqs.listener.mapper;

import com.onclass.report.model.bootcampreport.BootcampCreated;
import com.onclass.report.sqs.listener.dto.BootcampCreatedDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BootcampReportMapper {
    BootcampCreated toModel(BootcampCreatedDto bootcampCreatedDto);
}
