package com.saccoplus.controller;

import com.saccoplus.entity.User;
import com.saccoplus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        return ResponseEntity.ok("User deactivated successfully");
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(updatedUser.getRole());
        userRepository.save(user);
        return ResponseEntity.ok("User updated successfully");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        long totalUsers = userRepository.count();
        return ResponseEntity.ok("Total users: " + totalUsers);
    }
}