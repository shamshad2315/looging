package com.ulpf.controller;

import com.ulpf.dto.auth.AuthResponse;
import com.ulpf.dto.auth.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Demo authentication mode for framework verification
        String role = "ADMIN";
        if ("analyst".equalsIgnoreCase(request.getUsername())) {
            role = "ANALYST";
        }
        String token = "mock-jwt-token-ulpf-" + request.getUsername() + "-" + System.currentTimeMillis();
        return ResponseEntity.ok(new AuthResponse(token, request.getUsername(), role));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, String>> me() {
        return ResponseEntity.ok(Map.of("username", "admin", "role", "ADMIN", "status", "AUTHENTICATED"));
    }
}
