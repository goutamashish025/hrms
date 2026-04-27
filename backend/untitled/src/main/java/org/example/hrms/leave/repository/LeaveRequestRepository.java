package org.example.hrms.leave.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.hrms.leave.entity.LeaveRequest;
import org.example.hrms.leave.enums.LeaveStatus;
import org.example.hrms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    // Employee's own leaves
    List<LeaveRequest> findByEmployee(User employee);

    // All leaves by status (admin usage)
    List<LeaveRequest> findByStatus(LeaveStatus status);

    // Prevent overlapping leave
    boolean existsByEmployeeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            User employee,
            LocalDate endDate,
            LocalDate startDate
    );

    // ✅ NEW — Manager can see only his team pending leaves
    List<LeaveRequest> findByEmployee_ManagerAndStatus(
            User manager,
            LeaveStatus status
    );
}