package org.example.hrms.leave.controller;
import lombok.RequiredArgsConstructor;
import org.example.hrms.leave.enums.LeaveStatus;
import org.example.hrms.leave.service.LeaveService;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.example.hrms.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/leaves")
@RequiredArgsConstructor

public class ManagerLeaveController {

    private final LeaveService leaveService;
    private final UserRepository userRepository;

    @GetMapping("/pending")
    public ResponseEntity<?> getMyTeamPendingLeaves() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User manager = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                leaveService.getPendingLeavesForManager(manager)
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveLeave(@PathVariable Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User manager = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                leaveService.updateLeaveStatus(id, manager, LeaveStatus.APPROVED)
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectLeave(@PathVariable Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User manager = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                leaveService.updateLeaveStatus(id, manager, LeaveStatus.REJECTED)
        );
    }
}
