package com.unistay.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility for hashing and verifying user passwords using BCrypt.
 * BCrypt is industry-standard: it is slow by design (resistant to brute-force)
 * and automatically handles salting internally.
 */
public class PasswordUtil {

    // BCryptPasswordEncoder with default strength (10 rounds) – good for a university project
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * Hashes a raw plaintext password using BCrypt.
     * The returned string includes the salt and hash – safe to store directly in the DB.
     */
    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return ENCODER.encode(rawPassword);
    }

    /**
     * Verifies a raw plaintext password against a stored BCrypt hash.
     */
    public static boolean verifyPassword(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        return ENCODER.matches(rawPassword, storedHash);
    }
}
