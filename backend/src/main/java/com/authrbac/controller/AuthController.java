package com.authrbac.controller;

import com.authrbac.dto.AuthResponse;
import com.authrbac.dto.LoginRequest;
import com.authrbac.dto.RegisterRequest;
import com.authrbac.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Public endpoints - no authentication required for register or login
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register and login endpoints")
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/register
    // Anyone can call this to create a new account
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates user and returns JWT token")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // POST /api/auth/login
    // Accepts email + password, returns JWT if valid
    @PostMapping("/login")
    @Operation(summary = "Login with email and password", description = "Returns JWT token on success")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
