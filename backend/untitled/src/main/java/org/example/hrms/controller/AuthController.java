package org.example.hrms.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.hrms.dto.ApiResponse;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.example.hrms.security.JwtUtil;
import org.example.hrms.security.TokenBlacklistService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // REGISTER
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new ApiResponse<>("error", "Email is already in use!", null);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);

            return new ApiResponse<>("success", "User registered successfully!", savedUser);

        } catch (DataIntegrityViolationException e) {
            return new ApiResponse<>("error", "Duplicate email detected!" + e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>("error", "Could not register user: " + e.getMessage(), null);
        }
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            tokenBlacklistService.blacklist(token, jwtUtil.extractExpiration(token));

            return new ApiResponse<>("success", "Logout successful", null);
        }
        return new ApiResponse<>("error", "No token provided", null);
    }


    // LOGIN
//    @PostMapping("/login")
//    public ApiResponse<String> login(@RequestBody User user) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
//            );
//
//            if (authentication.isAuthenticated()) {
//                String token = jwtUtil.generateToken(user.getEmail());
//
//                return new ApiResponse<>("success", "Login successful", token);
//            } else {
//                return new ApiResponse<>("error", "Invalid credentials!", null);
//            }
//        } catch (AuthenticationException e) {
//            return new ApiResponse<>("error", "Invalid username or password!", null);
//        }
//    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                // Generate token
                String token = jwtUtil.generateToken(user.getEmail());

                // Fetch user from DB
                User dbUser = userRepository.findByEmail(user.getEmail()).orElse(null);

                // Build response map
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
//                responseData.put("user", dbUser);
                if (dbUser != null) {
                    responseData.put("employee_code", dbUser.getEmployeeCode());
                    responseData.put("id", dbUser.getId());
                    responseData.put("firstName", dbUser.getFirstName());
                    responseData.put("lastName", dbUser.getLastName());
                    responseData.put("email", dbUser.getEmail());
                    responseData.put("phone", dbUser.getPhone());
                    responseData.put("designation", dbUser.getDesignation());
                    responseData.put("department", dbUser.getDepartment());
                    responseData.put("salary", dbUser.getSalary());
                    responseData.put("dateOfJoining", dbUser.getDateOfJoining());
                    responseData.put("role", dbUser.getRole());
                    responseData.put("active", dbUser.getActive());
                }

                return new ApiResponse<>("success", "Login successful", responseData);
            } else {
                return new ApiResponse<>("error", "Invalid credentials!", null);
            }
        } catch (AuthenticationException e) {
            return new ApiResponse<>("error", "Invalid username or password!", null);
        }
    }
}
