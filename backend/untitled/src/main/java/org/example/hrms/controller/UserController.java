package org.example.hrms.controller;

import org.example.hrms.exception.ResourceNotFoundException;
import org.example.hrms.model.User;
import org.example.hrms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users
    @GetMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER','ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER','ADMIN')")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    // Create new user
    @PostMapping
    @PreAuthorize("hasAnyRole('HR_MANAGER','ADMIN')")
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Update user
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_MANAGER','ADMIN')")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setDesignation(userDetails.getDesignation());
        user.setDepartment(userDetails.getDepartment());
        return userRepository.save(user);
    }

    // Delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "User with ID " + id + " deleted successfully.";
    }
}
