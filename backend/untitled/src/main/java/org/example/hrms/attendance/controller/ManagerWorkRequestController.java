package org.example.hrms.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.enums.WorkRequestStatus;
import org.example.hrms.attendance.service.WorkRequestService;
import org.example.hrms.exception.ResourceNotFoundException;
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

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @GetMapping("/pending")
    public ResponseEntity<?> pending() {
        return ResponseEntity.ok(
                workRequestService.getPendingRequests(getLoggedInUser())
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        return ResponseEntity.ok(
                workRequestService.updateStatus(id, getLoggedInUser(), WorkRequestStatus.APPROVED)
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        return ResponseEntity.ok(
                workRequestService.updateStatus(id, getLoggedInUser(), WorkRequestStatus.REJECTED)
        );
    }
}
