package org.example.hrms.leave.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveResponseDTO {

    private Long id;
    private String employeeName;
    private String leaveType;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalDays;
    private String reason;
}