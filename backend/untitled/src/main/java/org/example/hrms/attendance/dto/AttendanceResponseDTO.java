package org.example.hrms.attendance.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceResponseDTO {

    private Long id;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String status;
}
