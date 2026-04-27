package org.example.hrms.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.service.WorkRequestService;
import org.example.hrms.leave.enums.LeaveStatus;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/work-request")
@RequiredArgsConstructor
public class ManagerWorkRequestController {

    private final WorkRequestService workRequestService;
    private final UserRepository userRepository;

    @GetMapping("/pending")
    public ResponseEntity<?> pending() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User manager = userRepository.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(
                workRequestService.getPendingRequests(manager)
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User manager = userRepository.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(
                workRequestService.updateStatus(id, manager, LeaveStatus.APPROVED)
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User manager = userRepository.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(
                workRequestService.updateStatus(id, manager, LeaveStatus.REJECTED)
        );
    }
}
