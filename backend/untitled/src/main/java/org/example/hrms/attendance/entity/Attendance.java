package org.example.hrms.attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.model.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    private LocalDate date;

    private LocalTime checkIn;

    private LocalTime checkOut;

    private String status; // PRESENT, LATE, ABSENT
}