package org.example.hrms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RequestDTO {

    private Long id;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}