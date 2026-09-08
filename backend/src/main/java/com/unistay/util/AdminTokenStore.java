package com.unistay.util;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory store for admin session tokens.
 *
 * Each token is a random UUID mapped to an expiry timestamp.
 * Tokens expire after 2 hours of inactivity-free use.
 *
 * NOTE: This is a simple university-project approach.
 * A production system would use a database-backed session or JWT.
 */
@Component
public class AdminTokenStore {

    private static final long TOKEN_TTL_MS = 2 * 60 * 60 * 1000L; // 2 hours

    // token (UUID string) → expiry time (epoch ms)
    private final ConcurrentHashMap<String, Long> store = new ConcurrentHashMap<>();

    /**
     * Creates and stores a new admin session token.
     * @return the generated token string
     */
    public String createToken() {
        String token = UUID.randomUUID().toString();
        store.put(token, System.currentTimeMillis() + TOKEN_TTL_MS);
        return token;
    }

    /**
     * Checks whether a token is present and not yet expired.
     */
    public boolean isValid(String token) {
        if (token == null) return false;
        Long expiry = store.get(token);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            store.remove(token);
            return false;
        }
        return true;
    }

    /**
     * Removes (invalidates) a token – used on logout.
     */
    public void invalidate(String token) {
        if (token != null) {
            store.remove(token);
        }
    }
}
