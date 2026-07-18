package org.example.hrms.leave.service;

import lombok.RequiredArgsConstructor;
import org.example.hrms.exception.ConflictException;
import org.example.hrms.exception.ForbiddenOperationException;
import org.example.hrms.exception.ResourceNotFoundException;
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

    public List<LeaveResponseDTO> getPendingLeavesForManager(User manager) {

        List<LeaveRequest> leaves =
                leaveRequestRepository
                        .findByEmployee_ManagerAndStatus(manager, LeaveStatus.PENDING);

        return leaves.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public LeaveResponseDTO applyLeave(User employee,
                                   Long leaveTypeId,
                                   LocalDate startDate,
                                   LocalDate endDate,
                                   Boolean isHalfDay,
                                   String reason) {

        // 1️⃣ Get Leave Type
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave Type not found"));

        // 2️⃣ Date validation
        if (Boolean.TRUE.equals(isHalfDay)) {
            if (!startDate.equals(endDate)) {
                throw new IllegalArgumentException("Half-day leave must have the same start and end date");
            }
        } else if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // 3️⃣ Strict Overlap Check
        boolean overlap = leaveRequestRepository
                .existsByEmployeeAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        employee,
                        endDate,
                        startDate
                );

        if (overlap) {
            throw new ConflictException("Leave dates overlap with existing leave");
        }

        // 4️⃣ Get Leave Balance
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeAndLeaveType(employee, leaveType)
                .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));

        // 5️⃣ Calculate total days
        double totalDays = Boolean.TRUE.equals(isHalfDay)
                ? 0.5
                : ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // 6️⃣ Check remaining balance
        if (balance.getRemainingLeaves() < totalDays) {
            throw new ConflictException("Insufficient leave balance");
        }

        // 7️⃣ Create leave request
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

        return toResponseDTO(leaveRequestRepository.save(leaveRequest));
    }

    @Transactional
    public LeaveResponseDTO updateLeaveStatus(Long leaveId, User manager, LeaveStatus newStatus) {

        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

        if (leave.getEmployee().getManager() == null ||
                !leave.getEmployee().getManager().getId().equals(manager.getId())) {
            throw new ForbiddenOperationException("You are not authorized to approve this leave");
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new ConflictException("Leave already processed");
        }

        if (newStatus == LeaveStatus.APPROVED) {
            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeAndLeaveType(leave.getEmployee(), leave.getLeaveType())
                    .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));

            double days = leave.getTotalDays();

            if (balance.getRemainingLeaves() < days) {
                throw new ConflictException("Not enough leave balance");
            }

            balance.setRemainingLeaves(balance.getRemainingLeaves() - days);
            balance.setUsedLeaves(balance.getUsedLeaves() + days);
            leaveBalanceRepository.save(balance);
        }

        leave.setStatus(newStatus);
        leave.setApprovedBy(manager);

        leaveRequestRepository.save(leave);

        return toResponseDTO(leave);
    }

    @Transactional
    public LeaveResponseDTO cancelLeave(Long leaveId, User employee) {

        LeaveRequest leave = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

        // 🔐 Only owner can cancel
        if (!leave.getEmployee().getId().equals(employee.getId())) {
            throw new ForbiddenOperationException("You are not allowed to cancel this leave");
        }

        // ❌ Cannot cancel rejected
        if (leave.getStatus() == LeaveStatus.REJECTED) {
            throw new ConflictException("Rejected leave cannot be cancelled");
        }

        // 🔁 If approved → restore balance
        if (leave.getStatus() == LeaveStatus.APPROVED) {

            LeaveBalance balance = leaveBalanceRepository
                    .findByEmployeeAndLeaveType(
                            leave.getEmployee(),
                            leave.getLeaveType())
                    .orElseThrow(() -> new ResourceNotFoundException("Leave balance not found"));

            double days = leave.getTotalDays();

            balance.setRemainingLeaves(balance.getRemainingLeaves() + days);
            balance.setUsedLeaves(balance.getUsedLeaves() - days);

            leaveBalanceRepository.save(balance);
        }

        leave.setStatus(LeaveStatus.CANCELLED);
        leaveRequestRepository.save(leave);

        return toResponseDTO(leave);
    }

    public List<LeaveResponseDTO> getLeavesByUser(User user) {
        return leaveRequestRepository.findByEmployee(user).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    private LeaveResponseDTO toResponseDTO(LeaveRequest leave) {
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
}
