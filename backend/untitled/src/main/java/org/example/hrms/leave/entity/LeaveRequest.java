package org.example.hrms.leave.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.hrms.leave.enums.HalfDayType;
import org.example.hrms.leave.enums.LeaveStatus;
import org.example.hrms.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private User employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)

    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isHalfDay;

    @Enumerated(EnumType.STRING)
    private HalfDayType halfDayType;

    private Double totalDays;
    private String reason;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    @JsonIgnore
    private User approvedBy;

    private LocalDate appliedDate;
}