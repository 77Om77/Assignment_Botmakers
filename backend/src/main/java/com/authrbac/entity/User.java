package com.authrbac.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// This is the main User table in our database.
// Each user has one role - either USER or ADMIN.
// We're keeping it simple: no separate roles table, just a string column.
@Entity
@Table(name = "users")  // "users" instead of "user" because USER is a reserved SQL keyword
@Data
@Builder                // lets us do User.builder().name("Alice").email("a@b.com").build()
@NoArgsConstructor      // JPA needs a no-arg constructor to create instances
@AllArgsConstructor     // needed by @Builder to work properly
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto-increment primary key
    private Long id;

    @Column(nullable = false)
    private String name;

    // Email must be unique - we'll use it to find users at login time
    @Column(nullable = false, unique = true)
    private String email;

    // We store the hashed password, NEVER the plain text
    @Column(nullable = false)
    private String password;

    // Store role as a string in DB. EnumType.STRING means we store "USER" or "ADMIN"
    // instead of ordinal (0 or 1) - safer if enum order changes
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERole role;
}
