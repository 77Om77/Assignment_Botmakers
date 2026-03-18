package com.authrbac.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// This filter runs once for every HTTP request.
// Its job: check if there's a valid JWT in the Authorization header.
// If yes: parse the token, load the user, and tell Spring Security
//         "this user is authenticated - let them through".
// If no JWT or invalid JWT: just continue to the next filter.
//         Spring Security will then deny access to protected routes.
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain  // the rest of the filter chain
    ) throws ServletException, IOException {

        // Step 1: Read the Authorization header
        // It should look like: "Bearer eyJhbGciOi..."
        String authHeader = request.getHeader("Authorization");

        // If there's no Authorization header or it doesn't start with "Bearer ",
        // skip JWT processing and move to next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 2: Extract the token (strip the "Bearer " prefix)
        String token = authHeader.substring(7);

        // Step 3: Extract the email from the token
        String email = jwtUtil.extractUsername(token);

        // Step 4: If we got an email AND the user isn't already authenticated
        // (SecurityContextHolder.getContext().getAuthentication() == null means not yet authenticated)
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user from the database
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Step 5: Validate the token (signature + expiry)
            if (jwtUtil.isTokenValid(token, userDetails)) {

                // Step 6: Build an authentication token that Spring Security understands
                // This is the standard object that represents an authenticated user
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,                         // no credentials needed at this point
                                userDetails.getAuthorities()  // roles like [ROLE_ADMIN]
                        );

                // Attach request details (IP address, etc.) to the token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Step 7: Set the authentication in the security context
                // This tells Spring Security: "this user is authenticated"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 8: Continue to the next filter (or to the actual controller)
        filterChain.doFilter(request, response);
    }
}
