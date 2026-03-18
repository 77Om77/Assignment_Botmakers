package com.authrbac.repository;

import com.authrbac.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Spring Data JPA repository for the User entity.
// We only need to define custom queries here - find, save, delete come for free.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Used during login to look up the user by email
    Optional<User> findByEmail(String email);

    // Used when registering to check if the email is already taken
    boolean existsByEmail(String email);
}
