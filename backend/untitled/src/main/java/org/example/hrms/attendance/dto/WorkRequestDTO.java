package org.example.hrms.attendance.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.hrms.attendance.enums.AttendanceStatus;

import java.time.LocalDate;

@Getter
@Setter
public class WorkRequestDTO {

    private LocalDate date;

    private AttendanceStatus type; // WFH or ON_DUTY

    private String reason;
}