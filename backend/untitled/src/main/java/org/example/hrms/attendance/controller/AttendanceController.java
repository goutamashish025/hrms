package org.example.hrms.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.service.AttendanceService;
import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    private User getLoggedInUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // ✅ CHECK IN
    @PostMapping("/check-in")
    public ResponseEntity<?> checkIn() {
        return ResponseEntity.ok(attendanceService.checkIn(getLoggedInUser()));
    }

    // ✅ CHECK OUT
    @PostMapping("/check-out")
    public ResponseEntity<?> checkOut() {
        return ResponseEntity.ok(attendanceService.checkOut(getLoggedInUser()));
    }

    // ✅ MY ATTENDANCE
    @GetMapping("/my")
    public ResponseEntity<?> myAttendance() {
        return ResponseEntity.ok(attendanceService.getMyAttendance(getLoggedInUser()));
    }
}