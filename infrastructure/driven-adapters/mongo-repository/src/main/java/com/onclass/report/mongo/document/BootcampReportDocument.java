package com.onclass.report.mongo.document;

import com.onclass.report.model.bootcampreport.CapabilityReport;
import com.onclass.report.model.bootcampreport.StudentReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bootcamp_reports")
public class BootcampReportDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long bootcampId;

    private String name;
    private String description;

    private Integer capabilityCount;
    private Integer technologyCount;

    @Indexed
    private Integer enrolledStudentCount;

    private List<CapabilityReport> capabilities;
    private List<StudentReport> students;
}