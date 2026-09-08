package com.unistay.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility for hashing and verifying user passwords securely.
 */
public class PasswordUtil {

    private static final int SALT_LENGTH = 16;

    /**
     * Hashes raw text password using SHA-256 with a randomly generated salt.
     * Returns string formatted as "salt:hash".
     */
    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        try {
            byte[] saltBytes = new byte[SALT_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(saltBytes);

            String saltBase64 = Base64.getEncoder().encodeToString(saltBytes);
            String hashBase64 = hashWithSalt(rawPassword, saltBytes);

            return saltBase64 + ":" + hashBase64;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies raw text password against stored "salt:hash" string.
     */
    public static boolean verifyPassword(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null || !storedHash.contains(":")) {
            return false;
        }

        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 2) return false;

            String saltBase64 = parts[0];
            String expectedHash = parts[1];

            byte[] saltBytes = Base64.getDecoder().decode(saltBase64);
            String computedHash = hashWithSalt(rawPassword, saltBytes);

            return expectedHash.equals(computedHash);
        } catch (Exception e) {
            return false;
        }
    }

    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}
