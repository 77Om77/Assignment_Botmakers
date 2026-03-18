package com.authrbac.controller;

import com.authrbac.dto.AuthResponse;
import com.authrbac.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Protected endpoint - requires a valid JWT.
// Both USER and ADMIN roles can access this.
// Access rules are enforced by SecurityConfig, not here.
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Requires USER or ADMIN role")
public class UserController {

    private final UserService userService;

    // GET /api/user
    // Returns a welcome message for authenticated users
    @GetMapping
    @Operation(
            summary = "User dashboard data",
            description = "Accessible to USER and ADMIN roles",
            security = @SecurityRequirement(name = "bearerAuth")  // tells Swagger to require Bearer token
    )
    public ResponseEntity<Map<String, String>> userEndpoint() {
        return ResponseEntity.ok(Map.of(
                "message", "Welcome to the User area! You are authenticated.",
                "Role", "USER"
        ));
    }

    // GET /api/user/me
    // Returns the current user's profile info
    @GetMapping("/me")
    @Operation(
            summary = "Get current user profile",
            description = "Returns name, email, role of the logged-in user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<AuthResponse> getMe() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
