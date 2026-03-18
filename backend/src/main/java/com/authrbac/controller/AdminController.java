package com.authrbac.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Admin-only endpoint - only ADMIN role can access this.
// If a USER tries to access this, Spring Security returns 403 Forbidden.
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Requires ADMIN role only")
public class AdminController {

    @GetMapping
    @Operation(
            summary = "Admin dashboard data",
            description = "Only accessible to users with ADMIN role",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<Map<String, String>> adminEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to the Admin panel! You have elevated privileges.",
                "Role", "ADMIN",
                "tip", "You can manage users, roles, and system settings from here."
        ));
    }
}
