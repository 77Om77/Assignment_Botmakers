package com.authrbac.service;

import com.authrbac.dto.AuthResponse;
import com.authrbac.dto.LoginRequest;
import com.authrbac.dto.RegisterRequest;
import com.authrbac.entity.ERole;
import com.authrbac.entity.User;
import com.authrbac.mapper.UserMapper;
import com.authrbac.repository.UserRepository;
import com.authrbac.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;

    // ==========================
    // REGISTER USER
    // ==========================
    public AuthResponse register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        // SAFE ROLE HANDLING (Fixed bug here)
        ERole role;
        if (request.getRole() == null || request.getRole().isBlank()) {
            role = ERole.USER;
        } else {
            try {
                role = ERole.valueOf(request.getRole().toUpperCase());
            } catch (Exception e) {
                role = ERole.USER; // fallback if invalid role
            }
        }

        // Create user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        // Save user
        User savedUser = userRepository.save(user);

        // Generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        // Map to response
        AuthResponse response = userMapper.toAuthResponse(savedUser);
        response.setToken(token);

        return response;
    }

    // ==========================
    // LOGIN USER
    // ==========================
    public AuthResponse login(LoginRequest request) {

        // Authenticate user (Spring Security handles password check)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Fetch user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        // Map to response
        AuthResponse response = userMapper.toAuthResponse(user);
        response.setToken(token);

        return response;
    }
}