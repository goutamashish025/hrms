package org.example.hrms.leave.controller;

import lombok.RequiredArgsConstructor;
import org.example.hrms.leave.dto.ApplyLeaveRequest;
import org.example.hrms.leave.entity.LeaveRequest;
import org.example.hrms.leave.service.LeaveService;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final UserRepository userRepository;

    // ✅ APPLY LEAVE
    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(@RequestBody ApplyLeaveRequest request) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User employee = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveRequest leaveRequest = leaveService.applyLeave(
                employee,
                request.getLeaveTypeId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getIsHalfDay(),
                request.getReason()
        );

        return ResponseEntity.ok(leaveRequest);
    }

    // ✅ GET MY LEAVES
    @GetMapping("/my")
    public ResponseEntity<?> getMyLeaves() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User employee = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<LeaveRequest> leaves = leaveService.getLeavesByUser(employee);

        return ResponseEntity.ok(leaves);
    }

    // ✅ CANCEL LEAVE
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelLeave(@PathVariable Long id) {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User employee = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        leaveService.cancelLeave(id, employee);

        return ResponseEntity.ok("Leave cancelled successfully");
    }

    @GetMapping("/types")
    public ResponseEntity<?> getLeaveTypes() {
        return ResponseEntity.ok(leaveService.getAllLeaveTypes());
    }
}