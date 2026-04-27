package org.example.hrms.attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.hrms.attendance.enums.AttendanceStatus;
import org.example.hrms.leave.enums.LeaveStatus;
import org.example.hrms.model.User;

import java.time.LocalDate;

@Entity
@Table(name = "work_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus type; // WFH or ON_DUTY

    @Enumerated(EnumType.STRING)
    private LeaveStatus status; // PENDING, APPROVED, REJECTED

    private String reason;

    private LocalDate appliedDate;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;
}
