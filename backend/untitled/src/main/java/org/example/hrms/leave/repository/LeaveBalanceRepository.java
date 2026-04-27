package org.example.hrms.leave.repository;

import org.example.hrms.leave.entity.LeaveBalance;
import org.example.hrms.leave.entity.LeaveType;
import org.example.hrms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByEmployeeAndLeaveType(User employee, LeaveType leaveType);
}