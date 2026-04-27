package org.example.hrms.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.entity.WorkRequest;
import org.example.hrms.attendance.enums.AttendanceStatus;
import org.example.hrms.attendance.repository.WorkRequestRepository;
import org.example.hrms.leave.enums.LeaveStatus;
import org.example.hrms.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkRequestService {

    private final WorkRequestRepository workRequestRepository;

    // 🧑 Employee apply WFH / OD
    public WorkRequest applyWorkRequest(User employee,
                                        LocalDate date,
                                        AttendanceStatus type,
                                        String reason) {

        if (type != AttendanceStatus.WFH &&
                type != AttendanceStatus.ON_DUTY) {
            throw new RuntimeException("Invalid work request type");
        }

        WorkRequest request = WorkRequest.builder()
                .employee(employee)
                .date(date)
                .type(type)
                .status(LeaveStatus.PENDING)
                .reason(reason)
                .appliedDate(LocalDate.now())
                .build();

        return workRequestRepository.save(request);
    }

    // 👨‍💼 Manager view pending
    public List<WorkRequest> getPendingRequests(User manager) {
        return workRequestRepository
                .findByEmployee_ManagerAndStatus(manager, LeaveStatus.PENDING);
    }

    // 👨‍💼 Manager approve / reject
    public WorkRequest updateStatus(Long id,
                                    User manager,
                                    LeaveStatus newStatus) {

        WorkRequest request = workRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getEmployee().getManager().getId()
                .equals(manager.getId())) {
            throw new RuntimeException("Not authorized");
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Already processed");
        }

        request.setStatus(newStatus);
        request.setApprovedBy(manager);

        return workRequestRepository.save(request);
    }
}