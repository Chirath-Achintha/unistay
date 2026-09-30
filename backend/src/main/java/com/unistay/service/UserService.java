package com.unistay.service;

import com.unistay.dto.OwnerRegistrationDTO;
import com.unistay.dto.StudentRegistrationDTO;
import com.unistay.dto.UserResponseDTO;
import com.unistay.entity.User;
import com.unistay.entity.UserRole;
import com.unistay.repository.UserRepository;
import com.unistay.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

/**
 * Service handling User Registration and management business logic.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new Student user account.
     */
    @Transactional
    public UserResponseDTO registerStudent(StudentRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail().toLowerCase().trim())) {
            throw new IllegalArgumentException("Email address '" + dto.getEmail() + "' is already registered.");
        }

        User user = new User();
        user.setFullName(dto.getFullName().trim());
        user.setEmail(dto.getEmail().toLowerCase().trim());
        user.setPhone(dto.getPhone().trim());
        user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        user.setRole(UserRole.STUDENT);
        user.setUniversity(dto.getUniversity().trim());
        user.setGender(dto.getGender().trim());

        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser);
    }

    /**
     * Registers a new Boarding Owner user account.
     */
    @Transactional
    public UserResponseDTO registerOwner(OwnerRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail().toLowerCase().trim())) {
            throw new IllegalArgumentException("Email address '" + dto.getEmail() + "' is already registered.");
        }

        User user = new User();
        user.setFullName(dto.getFullName().trim());
        user.setEmail(dto.getEmail().toLowerCase().trim());
        user.setPhone(dto.getPhone().trim());
        user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        user.setRole(UserRole.OWNER);
        user.setNic(dto.getNic().trim());
        user.setAddress(dto.getAddress().trim());

        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser);
    }

    /**
     * Checks if an email is already registered.
     */
    @Transactional(readOnly = true)
    public boolean isEmailRegistered(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userRepository.existsByEmail(email.toLowerCase().trim());
    }

    /**
     * Authenticates a user by email and password.
     */
    @Transactional(readOnly = true)
    public UserResponseDTO login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        
        if (!PasswordUtil.verifyPassword(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        
        return new UserResponseDTO(user);
    }

    /**
     * Retrieves a user by their ID.
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return new UserResponseDTO(user);
    }

    /**
     * Updates profile details for a user (fullName, phone, and role-specific fields).
     */
    @Transactional
    public UserResponseDTO updateProfile(Long id, Map<String, String> fields) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (fields.containsKey("fullName") && !fields.get("fullName").isBlank()) {
            user.setFullName(fields.get("fullName").trim());
        }
        if (fields.containsKey("phone") && !fields.get("phone").isBlank()) {
            user.setPhone(fields.get("phone").trim());
        }
        // Student-specific
        if (fields.containsKey("university")) {
            user.setUniversity(fields.get("university").trim());
        }
        if (fields.containsKey("gender")) {
            user.setGender(fields.get("gender").trim());
        }
        // Owner-specific
        if (fields.containsKey("nic")) {
            user.setNic(fields.get("nic").trim());
        }
        if (fields.containsKey("address")) {
            user.setAddress(fields.get("address").trim());
        }

        User saved = userRepository.save(user);
        return new UserResponseDTO(saved);
    }

    /**
     * Changes the password for a user after verifying the current password.
     */
    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (!PasswordUtil.verifyPassword(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }

        user.setPassword(PasswordUtil.hashPassword(newPassword));
        userRepository.save(user);
    }
}
