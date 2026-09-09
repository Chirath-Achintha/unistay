package com.unistay.controller;

import com.unistay.dto.ApiResponseDTO;
import com.unistay.dto.BoardingRequestDTO;
import com.unistay.dto.BoardingResponseDTO;
import com.unistay.service.BoardingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/boardings")
public class BoardingController {

    private final BoardingService boardingService;

    @Autowired
    public BoardingController(BoardingService boardingService) {
        this.boardingService = boardingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<BoardingResponseDTO>> createBoarding(
            @ModelAttribute BoardingRequestDTO dto,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {
        try {
            BoardingResponseDTO created = boardingService.createBoarding(dto, images);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("Boarding created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponseDTO<List<BoardingResponseDTO>>> getBoardingsByOwner(@PathVariable Long ownerId) {
        List<BoardingResponseDTO> boardings = boardingService.getBoardingsByOwner(ownerId);
        return ResponseEntity.ok(ApiResponseDTO.success("Boardings retrieved", boardings));
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<BoardingResponseDTO>>> searchBoardings(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minDistance,
            @RequestParam(required = false) Double maxDistance) {
        List<BoardingResponseDTO> boardings = boardingService.searchBoardings(
                city, university, roomType, minPrice, maxPrice, minDistance, maxDistance);
        return ResponseEntity.ok(ApiResponseDTO.success("Boardings found", boardings));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<BoardingResponseDTO>> getBoardingById(@PathVariable Long id) {
        try {
            BoardingResponseDTO boarding = boardingService.getBoardingById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Boarding retrieved", boarding));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<BoardingResponseDTO>> updateBoarding(
            @PathVariable Long id,
            @ModelAttribute BoardingRequestDTO dto,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {
        try {
            BoardingResponseDTO updated = boardingService.updateBoarding(id, dto, images);
            return ResponseEntity.ok(ApiResponseDTO.success("Boarding updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteBoarding(
            @PathVariable Long id,
            @RequestParam Long ownerId) {
        try {
            boardingService.deleteBoarding(id, ownerId);
            return ResponseEntity.ok(ApiResponseDTO.success("Boarding deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
