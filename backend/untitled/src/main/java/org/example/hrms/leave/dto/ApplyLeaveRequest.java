package org.example.hrms.leave.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplyLeaveRequest {

    private Long leaveTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isHalfDay;
    private String reason;
}