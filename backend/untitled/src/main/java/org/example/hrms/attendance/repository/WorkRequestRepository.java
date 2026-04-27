package org.example.hrms.attendance.repository;
import org.example.hrms.attendance.entity.WorkRequest;
import org.example.hrms.leave.enums.LeaveStatus;
import org.example.hrms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkRequestRepository extends JpaRepository<WorkRequest, Long> {

    List<WorkRequest> findByEmployee_ManagerAndStatus(User manager, LeaveStatus status);

    Optional<WorkRequest> findByEmployeeAndDateAndStatus(User employee,
                                                         LocalDate date,
                                                         LeaveStatus status);
}
