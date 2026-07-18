package org.example.hrms.controller;

import org.example.hrms.dto.ApiResponse;
import org.example.hrms.dto.RequestDTO;
import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.example.hrms.service.RequestService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;
    private final UserRepository userRepository;

    public RequestController(RequestService requestService,
                             UserRepository userRepository) {
        this.requestService = requestService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my")
    public ApiResponse<List<RequestDTO>> getMyRequests(Authentication authentication) {

        String email = authentication.getName();

        // ✅ REAL DB CALL
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<RequestDTO> data = requestService.getMyRequests(user.getId());

        return new ApiResponse<>("success", "Fetched successfully", data);
    }
}