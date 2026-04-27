package org.example.hrms.leave.service;

import lombok.RequiredArgsConstructor;
import org.example.hrms.leave.dto.LeaveResponseDTO;
import org.example.hrms.leave.entity.LeaveBalance;
import org.example.hrms.leave.entity.LeaveRequest;
import org.example.hrms.leave.entity.LeaveType;
import org.example.hrms.leave.enums.LeaveStatus;
import org.example.hrms.leave.repository.LeaveBalanceRepository;
import org.example.hrms.leave.repository.LeaveRequestRepository;
import org.example.hrms.leave.repository.LeaveTypeRepository;
import org.example.hrms.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveTypeRepository leaveTypeRepository;

//    public List<LeaveRequest> getPendingLeavesForManager(User manager) {
//        return leaveRequestRepository
//                .findByEmployee_ManagerAndStatus(manager, LeaveStatus.PENDING);
//    }

    public List<LeaveResponseDTO> getPendingLeavesForManager(User manager) {

        List<LeaveRequest> leaves =
                leaveRequestRepository
                        .findByEmployee_ManagerAndStatus(manager, LeaveStatus.PENDING);

        return leaves.stream()
                .map(leave -> LeaveResponseDTO.builder()
                        .id(leave.getId())
                        .employeeName(
                                leave.getEmployee().getFirstName() + " " +
                                        leave.getEmployee().getLastName()
                        )
                        .leaveType(leave.getLeaveType().getName())
                        .status(leave.getStatus().name())
                        .startDate(leave.getStartDate())
                        .endDate(leave.getEndDate())
                        .totalDays(leave.getTotalDays())
                        .reason(leave.getReason())
                        .build()
                )
                .toList();
    }

    public LeaveRequest approveLeave(Long leaveId, User manager) {

        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        // 🔥 Important: Only assigned manager can approve
        if (!leave.getEmployee().getManager().getId().equals(manager.getId())) {
            throw new RuntimeException("You are not authorized to approve this leave");
        }

        if (leave.getStatus() != LeaveStatus.PENDING)
            throw new RuntimeException("Leave already processed");

        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeAndLeaveType(
                        leave.getEmployee(),
                        leave.getLeaveType())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        long days = ChronoUnit.DAYS.between(
                leave.getStartDate(),
                leave.getEndDate()) + 1;

        if (balance.getRemainingLeaves() < days)
            throw new RuntimeException("Not enough leave balance");

        balance.setRemainingLeaves(balance.getRemainingLeaves() - days);
        balance.setUsedLeaves(balance.getUsedLeaves() + days);

        leave.setStatus(LeaveStatus.APPROVED);

        leaveBalanceRepository.save(balance);
        return leaveRequestRepository.save(leave);
    }

    @Transactional
    public LeaveRequest applyLeave(User employee,
                                   Long leaveTypeId,
                                   LocalDate startDate,
                                   LocalDate endDate,
                                   Boolean isHalfDay,
                                   String reason) {

        // 1️⃣ Get Leave Type
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave Type not found"));

        // 2️⃣ Strict Overlap Check
        boolean overlap = leaveRequestRepository
                .existsByEmployeeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        employee,
                        endDate,
                        startDate
                );

        if (overlap) {
            throw new RuntimeException("Leave dates overlap with existing leave");
        }

        // 3️⃣ Get Leave Balance
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeAndLeaveType(employee, leaveType)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        // 4️⃣ Calculate total days
        double totalDays;

        if (Boolean.TRUE.equals(isHalfDay)) {
            totalDays = 0.5;
        } else {
            totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }

        // 5️⃣ Check remaining balance
        if (balance.getRemainingLeaves() < totalDays) {
            throw new RuntimeException("Insufficient leave balance");
        }

        // 6️⃣ Create leave request
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .employee(employee)
                .leaveType(leaveType)
                .startDate(startDate)
                .endDate(endDate)
                .isHalfDay(isHalfDay)
                .totalDays(totalDays)
                .reason(reason)
                .status(LeaveStatus.PENDING)
                .appliedDate(LocalDate.now())
                .build();

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveResponseDTO updateLeaveStatus(Long leaveId, User manager, LeaveStatus newStatus) {

        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        // Check if manager is actually manager of employee
        if (!leave.getEmployee().getManager().getId().equals(manager.getId())) {
            throw new RuntimeException("You are not authorized to approve this leave");
        }

        // Only allow if pending
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Leave already processed");
        }

        leave.setStatus(newStatus);
        leave.setApprovedBy(manager);

        leaveRequestRepository.save(leave);

        return LeaveResponseDTO.builder()
                .id(leave.getId())
                .employeeName(
                        leave.getEmployee().getFirstName() + " " +
                                leave.getEmployee().getLastName()
                )
                .leaveType(leave.getLeaveType().getName())
                .status(leave.getStatus().name())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .totalDays(leave.getTotalDays())
                .reason(leave.getReason())
                .build();
    }


    @Transactional
    public LeaveResponseDTO cancelLeave(Long leaveId, User employee) {

        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        // 🔐 Only owner can cancel
        if (!leave.getEmployee().getId().equals(employee.getId())) {
            throw new RuntimeException("You are not allowed to cancel this leave");
        }

        // ❌ Cannot cancel rejected
        if (leave.getStatus() == LeaveStatus.REJECTED) {
            throw new RuntimeException("Rejected leave cannot be cancelled");
        }

        // 🔁 If approved → restore balance
        if (leave.getStatus() == LeaveStatus.APPROVED) {

            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeAndLeaveType(
                            leave.getEmployee(),
                            leave.getLeaveType())
                    .orElseThrow(() -> new RuntimeException("Leave balance not found"));

            double days = leave.getTotalDays();

            balance.setRemainingLeaves(balance.getRemainingLeaves() + days);
            balance.setUsedLeaves(balance.getUsedLeaves() - days);

            leaveBalanceRepository.save(balance);
        }

        leave.setStatus(LeaveStatus.CANCELLED);
        leaveRequestRepository.save(leave);

        return LeaveResponseDTO.builder()
                .id(leave.getId())
                .employeeName(
                        leave.getEmployee().getFirstName() + " " +
                                leave.getEmployee().getLastName()
                )
                .leaveType(leave.getLeaveType().getName())
                .status(leave.getStatus().name())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .totalDays(leave.getTotalDays())
                .reason(leave.getReason())
                .build();
    }

    public List<LeaveRequest> getLeavesByUser(User user) {
        return leaveRequestRepository.findByEmployee(user);
    }

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }


}