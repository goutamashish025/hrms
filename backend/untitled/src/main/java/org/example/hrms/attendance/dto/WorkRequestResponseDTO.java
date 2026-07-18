package org.example.hrms.attendance.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkRequestResponseDTO {

    private Long id;
    private String employeeName;
    private LocalDate date;
    private String type;
    private String status;
    private String reason;
}
