package com.authrbac.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Public endpoint - no authentication required.
// Useful for health checks, welcome messages, etc.
@RestController
@RequestMapping("/api/public")
@Tag(name = "Public", description = "No authentication required")
public class PublicController {

    @GetMapping
    @Operation(summary = "Public endpoint", description = "Accessible without any token")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Hello! This is a public endpoint. No login needed.",
                "status", "ok"
        ));
    }
}
