package com.authrbac.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// What the client sends when registering a new account
@Data  // Lombok generates getters, setters, equals, hashCode, toString
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;

    // Role should be "USER" or "ADMIN"
    // If not provided, we'll default to USER in the service layer
    private String role;
}
