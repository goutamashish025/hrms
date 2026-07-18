package org.example.hrms.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.dto.WorkRequestResponseDTO;
import org.example.hrms.attendance.entity.WorkRequest;
import org.example.hrms.attendance.enums.AttendanceStatus;
import org.example.hrms.attendance.enums.WorkRequestStatus;
import org.example.hrms.attendance.repository.WorkRequestRepository;
import org.example.hrms.exception.ConflictException;
import org.example.hrms.exception.ForbiddenOperationException;
import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkRequestService {

    private final WorkRequestRepository workRequestRepository;

    // 🧑 Employee apply WFH / OD
    @Transactional
    public WorkRequestResponseDTO applyWorkRequest(User employee,
                                        LocalDate date,
                                        AttendanceStatus type,
                                        String reason) {

        if (type != AttendanceStatus.WFH &&
                type != AttendanceStatus.ON_DUTY) {
            throw new IllegalArgumentException("Invalid work request type");
        }

        WorkRequest request = WorkRequest.builder()
                .employee(employee)
                .date(date)
                .type(type)
                .status(WorkRequestStatus.PENDING)
                .reason(reason)
                .appliedDate(LocalDate.now())
                .build();

        return toResponseDTO(workRequestRepository.save(request));
    }

    // 👨‍💼 Manager view pending
    public List<WorkRequestResponseDTO> getPendingRequests(User manager) {
        return workRequestRepository
                .findByEmployee_ManagerAndStatus(manager, WorkRequestStatus.PENDING)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // 👨‍💼 Manager approve / reject
    @Transactional
    public WorkRequestResponseDTO updateStatus(Long id,
                                    User manager,
                                    WorkRequestStatus newStatus) {

        WorkRequest request = workRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (request.getEmployee().getManager() == null ||
                !request.getEmployee().getManager().getId().equals(manager.getId())) {
            throw new ForbiddenOperationException("Not authorized");
        }

        if (request.getStatus() != WorkRequestStatus.PENDING) {
            throw new ConflictException("Already processed");
        }

        request.setStatus(newStatus);
        request.setApprovedBy(manager);

        return toResponseDTO(workRequestRepository.save(request));
    }

    private WorkRequestResponseDTO toResponseDTO(WorkRequest request) {
        return WorkRequestResponseDTO.builder()
                .id(request.getId())
                .employeeName(
                        request.getEmployee().getFirstName() + " " +
                                request.getEmployee().getLastName()
                )
                .date(request.getDate())
                .type(request.getType().name())
                .status(request.getStatus().name())
                .reason(request.getReason())
                .build();
    }

}
