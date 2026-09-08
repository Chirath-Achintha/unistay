package com.unistay.controller;

import com.unistay.dto.ApiResponseDTO;
import com.unistay.dto.StudentUpdateDTO;
import com.unistay.dto.UserResponseDTO;
import com.unistay.service.AdminStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Admin Student Management.
 *
 * All routes are protected by AdminAuthInterceptor which validates the X-Admin-Token header.
 *
 * GET    /api/admin/students         – list all students (optional ?search=)
 * GET    /api/admin/students/{id}    – get a specific student
 * PUT    /api/admin/students/{id}    – update student details
 * DELETE /api/admin/students/{id}    – delete a student
 */
@RestController
@RequestMapping("/api/admin/students")
public class AdminStudentController {

    private final AdminStudentService adminStudentService;

    @Autowired
    public AdminStudentController(AdminStudentService adminStudentService) {
        this.adminStudentService = adminStudentService;
    }

    /** GET /api/admin/students?search=optional */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UserResponseDTO>>> getStudents(
            @RequestParam(required = false) String search) {
        List<UserResponseDTO> students = adminStudentService.getStudents(search);
        return ResponseEntity.ok(ApiResponseDTO.success("Students retrieved successfully", students));
    }

    /** GET /api/admin/students/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getStudent(@PathVariable Long id) {
        UserResponseDTO student = adminStudentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Student retrieved successfully", student));
    }

    /** PUT /api/admin/students/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateStudent(
            @PathVariable Long id,
            @RequestBody StudentUpdateDTO updateDTO) {
        UserResponseDTO updated = adminStudentService.updateStudent(id, updateDTO);
        return ResponseEntity.ok(ApiResponseDTO.success("Student updated successfully", updated));
    }

    /** DELETE /api/admin/students/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteStudent(@PathVariable Long id) {
        adminStudentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Student deleted successfully"));
    }
}
