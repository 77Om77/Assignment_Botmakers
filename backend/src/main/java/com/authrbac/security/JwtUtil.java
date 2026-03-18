package com.authrbac.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// JwtUtil handles everything related to JWT:
// - Generating a token for a logged-in user
// - Validating a token from an incoming request
// - Extracting the username (email) from a token
@Component
public class JwtUtil {

    // Read the secret and expiration from application.properties
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    // Build the signing key from our secret string
    private Key getSigningKey() {
        // Keys.hmacShaKeyFor turns our string into a proper Key object for HS256
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Generate a JWT for the given user
    // The token contains the email as subject, and is signed with our secret key
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // We could add extra claims like role here if needed
        return buildToken(claims, userDetails.getUsername());
    }

    private String buildToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)                                         // extra data (empty for now)
                .setSubject(subject)                                       // email goes here
                .setIssuedAt(new Date(System.currentTimeMillis()))         // when was it created
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))  // when does it expire
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)       // sign it
                .compact();                                                // turn into a string
    }

    // Pull out the email (subject) from the token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Check if token is valid: username must match AND token must not be expired
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Parse the token and extract all claims inside it
    // This throws an exception if the token is tampered with or expired
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())   // use the same key we used to sign
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
