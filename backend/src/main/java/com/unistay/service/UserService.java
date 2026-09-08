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
}
