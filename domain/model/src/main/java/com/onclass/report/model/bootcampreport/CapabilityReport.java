package com.onclass.report.model.bootcampreport;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapabilityReport {
    private Long id;
    private String name;
    private List<TechnologyReport> technologies;
}
