package com.unistay.service;

import com.unistay.dto.StudentUpdateDTO;
import com.unistay.dto.UserResponseDTO;
import com.unistay.entity.User;
import com.unistay.entity.UserRole;
import com.unistay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Admin Student Management operations.
 * Handles listing, searching, viewing, updating, and deleting student accounts.
 */
@Service
public class AdminStudentService {

    private final UserRepository userRepository;

    @Autowired
    public AdminStudentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns all students, or students matching a search term if provided.
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getStudents(String search) {
        List<User> students;
        if (search != null && !search.trim().isEmpty()) {
            students = userRepository.searchStudents(search.trim());
        } else {
            students = userRepository.findByRole(UserRole.STUDENT);
        }
        return students.stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Returns one student by ID, or throws if not found / not a student.
     */
    @Transactional(readOnly = true)
    public UserResponseDTO getStudentById(Long id) {
        User user = findStudentOrThrow(id);
        return new UserResponseDTO(user);
    }

    /**
     * Updates editable student fields and saves to the database.
     */
    @Transactional
    public UserResponseDTO updateStudent(Long id, StudentUpdateDTO dto) {
        User user = findStudentOrThrow(id);

        if (dto.getFullName() != null && !dto.getFullName().trim().isEmpty()) {
            user.setFullName(dto.getFullName().trim());
        }
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            user.setPhone(dto.getPhone().trim());
        }
        if (dto.getUniversity() != null && !dto.getUniversity().trim().isEmpty()) {
            user.setUniversity(dto.getUniversity().trim());
        }
        if (dto.getGender() != null && !dto.getGender().trim().isEmpty()) {
            user.setGender(dto.getGender().trim());
        }

        User saved = userRepository.save(user);
        return new UserResponseDTO(saved);
    }

    /**
     * Deletes a student by ID.
     */
    @Transactional
    public void deleteStudent(Long id) {
        User user = findStudentOrThrow(id);
        userRepository.delete(user);
    }

    // ---- private helpers ----

    private User findStudentOrThrow(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + id + " not found."));
        if (user.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException("User with ID " + id + " is not a student.");
        }
        return user;
    }
}
