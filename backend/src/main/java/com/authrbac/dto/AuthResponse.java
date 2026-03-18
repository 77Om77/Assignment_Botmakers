package com.authrbac.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// What we send back to the client after login or register.
// Contains the JWT token + some basic user info so the frontend
// knows who is logged in and what role they have - without
// needing to make another API call.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;    // The JWT - client must store this and send it on each request
    private String name;     // User's display name
    private String email;    // User's email
    private String role;     // "USER" or "ADMIN" - frontend uses this for role-based UI
}
