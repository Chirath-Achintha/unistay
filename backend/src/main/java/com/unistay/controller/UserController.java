package com.unistay.controller;

import com.unistay.dto.ApiResponseDTO;
import com.unistay.dto.OwnerRegistrationDTO;
import com.unistay.dto.StudentRegistrationDTO;
import com.unistay.dto.UserResponseDTO;
import com.unistay.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for User Registration endpoints (Student & Boarding Owner).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users/register/student
     * Registers a new Student user.
     */
    @PostMapping("/register/student")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> registerStudent(
            @Valid @RequestBody StudentRegistrationDTO studentDto) {
        UserResponseDTO registeredUser = userService.registerStudent(studentDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Student account registered successfully!", registeredUser));
    }

    /**
     * POST /api/users/register/owner
     * Registers a new Boarding Owner user.
     */
    @PostMapping("/register/owner")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> registerOwner(
            @Valid @RequestBody OwnerRegistrationDTO ownerDto) {
        UserResponseDTO registeredUser = userService.registerOwner(ownerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Boarding Owner account registered successfully!", registeredUser));
    }

    /**
     * GET /api/users/check-email?email=user@example.com
     * Utility API to check if an email is already taken.
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDTO<Map<String, Boolean>>> checkEmail(@RequestParam String email) {
        boolean registered = userService.isEmailRegistered(email);
        Map<String, Boolean> result = new HashMap<>();
        result.put("registered", registered);
        return ResponseEntity.ok(ApiResponseDTO.success("Email status retrieved", result));
    }

    /**
     * POST /api/users/login
     * Authenticates a user (Student or Owner).
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> login(
            @RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("Email and password are required."));
        }

        try {
            UserResponseDTO user = userService.login(email, password);
            return ResponseEntity.ok(ApiResponseDTO.success("Login successful", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    /**
     * GET /api/users/{id}
     * Retrieves a user by their ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(@PathVariable Long id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("User retrieved successfully", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
