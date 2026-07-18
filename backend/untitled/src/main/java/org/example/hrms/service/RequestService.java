package org.example.hrms.service;

import org.example.hrms.dto.RequestDTO;
import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.leave.entity.LeaveRequest;
import org.example.hrms.leave.repository.LeaveRequestRepository;
import org.example.hrms.attendance.entity.WorkRequest;
import org.example.hrms.attendance.repository.WorkRequestRepository;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RequestService {

    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRepository;
    private final WorkRequestRepository workRequestRepository;

    public RequestService(UserRepository userRepository,
                          LeaveRequestRepository leaveRepository,
                          WorkRequestRepository workRequestRepository) {
        this.userRepository = userRepository;
        this.leaveRepository = leaveRepository;
        this.workRequestRepository = workRequestRepository;
    }

    public List<RequestDTO> getMyRequests(Long userId) {

        List<RequestDTO> result = new ArrayList<>();

        // 👉 Get logged in user
        User employee = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 👉 Get Leaves
        List<LeaveRequest> leaves = leaveRepository.findByEmployee(employee);

        for (LeaveRequest l : leaves) {
            result.add(new RequestDTO(
                    l.getId(),
                    "LEAVE",
                    l.getStartDate(),
                    l.getEndDate(),
                    l.getStatus().name()
            ));
        }

        // 👉 Get Work / WFH
        List<WorkRequest> works = workRequestRepository.findByEmployee(employee);

        for (WorkRequest w : works) {
            result.add(new RequestDTO(
                    w.getId(),
                    "WFH",
                    w.getDate(),
                    w.getDate(),
                    w.getStatus().name()
            ));
        }

        // 👉 Sort latest first
        result.sort(Comparator.comparing(RequestDTO::getStartDate).reversed());

        return result;
    }
}