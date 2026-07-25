package com.store.backend.controller;

import com.store.backend.entity.User;
import com.store.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, String>> getProfile(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "fullName", user.getName() != null ? user.getName() : "",
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "email", user.getEmail()
        ));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateProfile(
            Authentication auth, @RequestBody Map<String, String> body) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (body.get("fullName") != null) user.setName(body.get("fullName"));
        if (body.get("phone") != null) user.setPhone(body.get("phone"));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "fullName", user.getName() != null ? user.getName() : "",
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "email", user.getEmail()
        ));
    }
}