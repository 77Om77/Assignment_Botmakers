package com.authrbac.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// This is the main Spring Security configuration class.
// We define which endpoints are public, which require authentication,
// and which require specific roles.
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // enables @PreAuthorize on methods if needed
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    // Define which URLs are allowed and which are protected
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF - not needed for REST APIs (we use JWT, not cookies)
                .csrf(AbstractHttpConfigurer::disable)

                // Configure CORS so our React frontend can call this API
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // URL-level authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - anyone can access these
                        .requestMatchers(
                                "/api/auth/**",         // login and register
                                "/api/public/**",       // public content
                                "/swagger-ui/**",       // Swagger UI
                                "/swagger-ui.html",
                                "/api-docs/**",         // OpenAPI JSON
                                "/h2-console/**"        // H2 DB console for development
                        ).permitAll()

                        // Only ADMIN can access admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Both USER and ADMIN can access user endpoints
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                // Stateless: no HTTP sessions - we rely entirely on JWT
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Tell Spring Security to use our custom UserDetailsService + BCrypt
                .authenticationProvider(authenticationProvider())

                // Add our JWT filter BEFORE Spring's built-in username/password filter
                // This way, JWT is checked first on every request
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    // Wires together our UserDetailsService + BCrypt password encoder
    // Spring Security uses this to authenticate users during login
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // BCrypt is the standard hashing algorithm for passwords.
    // It's slow by design (10 rounds by default) which makes brute-force harder.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager is needed by AuthService to authenticate login requests
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // CORS configuration - allow our React frontend (on port 5173) to call our API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));  // Vite dev server
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
