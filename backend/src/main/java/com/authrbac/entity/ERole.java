package com.authrbac.entity;

// These are the roles a user can have in our system.
// We keep it simple: either USER or ADMIN.
// Using an enum prevents typos and makes role checks safe.
public enum ERole {
    USER,
    ADMIN
}
