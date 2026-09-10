package com.unistay.controller;

import com.unistay.dto.ApiResponseDTO;
import com.unistay.dto.VisitRequestDTO;
import com.unistay.dto.VisitResponseDTO;
import com.unistay.dto.VisitStatusUpdateDTO;
import com.unistay.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
public class VisitController {

    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/student/{studentId}")
    public ResponseEntity<ApiResponseDTO<VisitResponseDTO>> createVisitRequest(
            @PathVariable Long studentId,
            @RequestBody VisitRequestDTO requestDto) {
        try {
            VisitResponseDTO response = visitService.createVisitRequest(studentId, requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("Visit request created successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponseDTO<List<VisitResponseDTO>>> getStudentVisits(@PathVariable Long studentId) {
        List<VisitResponseDTO> visits = visitService.getVisitsForStudent(studentId);
        return ResponseEntity.ok(ApiResponseDTO.success("Retrieved student visits", visits));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponseDTO<List<VisitResponseDTO>>> getOwnerVisits(@PathVariable Long ownerId) {
        List<VisitResponseDTO> visits = visitService.getVisitsForOwner(ownerId);
        return ResponseEntity.ok(ApiResponseDTO.success("Retrieved owner visits", visits));
    }

    @PutMapping("/{visitId}/owner/{ownerId}/status")
    public ResponseEntity<ApiResponseDTO<VisitResponseDTO>> updateVisitStatusByOwner(
            @PathVariable Long visitId,
            @PathVariable Long ownerId,
            @RequestBody VisitStatusUpdateDTO updateDto) {
        try {
            VisitResponseDTO response = visitService.updateVisitStatusByOwner(visitId, ownerId, updateDto);
            return ResponseEntity.ok(ApiResponseDTO.success("Visit status updated", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/{visitId}/student/{studentId}/accept-proposed")
    public ResponseEntity<ApiResponseDTO<VisitResponseDTO>> acceptProposedDateByStudent(
            @PathVariable Long visitId,
            @PathVariable Long studentId) {
        try {
            VisitResponseDTO response = visitService.acceptProposedDateByStudent(visitId, studentId);
            return ResponseEntity.ok(ApiResponseDTO.success("Alternative date accepted", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
