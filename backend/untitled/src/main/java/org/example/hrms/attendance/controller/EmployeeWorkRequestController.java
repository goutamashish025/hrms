package org.example.hrms.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.hrms.attendance.dto.WorkRequestDTO;
import org.example.hrms.attendance.service.WorkRequestService;
import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee/work-request")
@RequiredArgsConstructor
public class EmployeeWorkRequestController {

    private final WorkRequestService workRequestService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> apply(@RequestBody WorkRequestDTO dto) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return ResponseEntity.ok(
                workRequestService.applyWorkRequest(
                        employee,
                        dto.getDate(),
                        dto.getType(),
                        dto.getReason()
                )
        );
    }

}
