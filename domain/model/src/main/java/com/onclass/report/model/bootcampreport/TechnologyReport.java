package com.onclass.report.model.bootcampreport;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TechnologyReport {
    private Long id;
    private String name;
}
