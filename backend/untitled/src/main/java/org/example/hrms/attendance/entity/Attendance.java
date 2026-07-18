package org.example.hrms.attendance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.attendance.enums.AttendanceStatus;
import org.example.hrms.model.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "date"})
})
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
    @JsonIgnore
    private User employee;

    private LocalDate date;

    private LocalTime checkIn;

    private LocalTime checkOut;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
}