package org.example.ybb.api;

import lombok.Data;
import org.example.ybb.domain.Subject;
@Data
public class Plan {
    private Subject subject;
    private Integer totalDays;
    private Integer completeDays;
}
