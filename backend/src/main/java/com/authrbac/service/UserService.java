package com.authrbac.service;

import com.authrbac.dto.AuthResponse;
import com.authrbac.entity.User;
import com.authrbac.mapper.UserMapper;
import com.authrbac.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

// UserService handles operations on the currently logged-in user.
// For now, just getting the current user's profile.
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Get the profile of whoever is currently authenticated.
    // SecurityContextHolder holds the current user's info after JwtAuthFilter runs.
    public AuthResponse getCurrentUser() {
        // The "principal" (authenticated user) name is the email set by JwtAuthFilter
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB"));

        // Return user info - no new token is generated here, frontend already has one
        AuthResponse response = userMapper.toAuthResponse(user);
        response.setToken(null);  // don't send a new token, not needed here
        return response;
    }
}
