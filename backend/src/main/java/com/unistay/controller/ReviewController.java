package com.unistay.controller;

import com.unistay.dto.ApiResponseDTO;
import com.unistay.dto.BoardingReviewsDTO;
import com.unistay.dto.ReviewRequestDTO;
import com.unistay.dto.ReviewResponseDTO;
import com.unistay.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/boarding/{boardingId}")
    public ResponseEntity<ApiResponseDTO<BoardingReviewsDTO>> getBoardingReviews(@PathVariable Long boardingId) {
        BoardingReviewsDTO reviewsDTO = reviewService.getBoardingReviews(boardingId);
        return ResponseEntity.ok(ApiResponseDTO.success("Reviews retrieved successfully", reviewsDTO));
    }

    @PostMapping("/boarding/{boardingId}/student/{studentId}")
    public ResponseEntity<ApiResponseDTO<ReviewResponseDTO>> createReview(
            @PathVariable Long boardingId,
            @PathVariable Long studentId,
            @RequestBody ReviewRequestDTO requestDTO) {
        try {
            ReviewResponseDTO response = reviewService.createReview(studentId, boardingId, requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponseDTO.success("Review posted successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @PutMapping("/{reviewId}/student/{studentId}")
    public ResponseEntity<ApiResponseDTO<ReviewResponseDTO>> updateReview(
            @PathVariable Long reviewId,
            @PathVariable Long studentId,
            @RequestBody ReviewRequestDTO requestDTO) {
        try {
            ReviewResponseDTO response = reviewService.updateReview(studentId, reviewId, requestDTO);
            return ResponseEntity.ok(ApiResponseDTO.success("Review updated successfully", response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{reviewId}/student/{studentId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteReview(
            @PathVariable Long reviewId,
            @PathVariable Long studentId) {
        try {
            reviewService.deleteReview(studentId, reviewId);
            return ResponseEntity.ok(ApiResponseDTO.success("Review deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(e.getMessage()));
        }
    }
}
