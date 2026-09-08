package com.unistay.controller;

import com.unistay.dto.ApiResponseDTO;
import com.unistay.util.AdminTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Admin Authentication.
 *
 * POST /api/admin/login   – validate credentials and issue a session token
 * POST /api/admin/logout  – invalidate the current session token
 *
 * Admin credentials are read from environment variables (ADMIN_EMAIL, ADMIN_PASSWORD)
 * via application.properties bindings. They are NEVER exposed in source code or JS.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Value("${spring.admin.email}")
    private String adminEmail;

    @Value("${spring.admin.password}")
    private String adminPassword;

    private final AdminTokenStore tokenStore;

    @Autowired
    public AdminController(AdminTokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * POST /api/admin/login
     * Body: { "email": "...", "password": "..." }
     * Returns: { "token": "uuid", "adminEmail": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> login(
            @RequestBody Map<String, String> credentials) {

        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("Email and password are required."));
        }

        // Direct string comparison against env-var-sourced values
        boolean emailMatch = adminEmail.equalsIgnoreCase(email.trim());
        boolean passwordMatch = adminPassword.equals(password);

        if (!emailMatch || !passwordMatch) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Invalid admin credentials. Please try again."));
        }

        String token = tokenStore.createToken();
        Map<String, String> responseData = Map.of(
                "token", token,
                "adminEmail", adminEmail
        );

        return ResponseEntity.ok(ApiResponseDTO.success("Admin login successful.", responseData));
    }

    /**
     * POST /api/admin/logout
     * Header: X-Admin-Token: <token>
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<Void>> logout(
            @RequestHeader(value = "X-Admin-Token", required = false) String token) {
        tokenStore.invalidate(token);
        return ResponseEntity.ok(ApiResponseDTO.success("Logged out successfully."));
    }
}
