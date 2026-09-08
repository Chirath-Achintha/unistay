package com.unistay.controller;

import com.unistay.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * System Controller for application health check and environment status.
 */
@RestController
@RequestMapping("/api")
public class SystemController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getHealthStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("appName", "UniStay - University Student Accommodation Platform");
        status.put("status", "UP");
        status.put("version", "1.0.0");
        status.put("environment", "Development");

        return ResponseEntity.ok(ApiResponseDTO.success("UniStay system is running smoothly.", status));
    }
}
