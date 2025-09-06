package org.example.hrms.controller;

import org.example.hrms.dto.ApiResponse;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.example.hrms.security.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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
            return new ApiResponse<>("error", "Duplicate email detected!", null);
        } catch (Exception e) {
            return new ApiResponse<>("error", "Could not register user: " + e.getMessage(), null);
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(user.getEmail());

                return new ApiResponse<>("success", "Login successful", token);
            } else {
                return new ApiResponse<>("error", "Invalid credentials!", null);
            }
        } catch (AuthenticationException e) {
            return new ApiResponse<>("error", "Invalid username or password!", null);
        }
    }
}
