package com.unistay.service;

import com.unistay.dto.BoardingReviewsDTO;
import com.unistay.dto.ReviewRequestDTO;
import com.unistay.dto.ReviewResponseDTO;
import com.unistay.entity.Boarding;
import com.unistay.entity.Review;
import com.unistay.entity.User;
import com.unistay.repository.BoardingRepository;
import com.unistay.repository.ReviewRepository;
import com.unistay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BoardingRepository boardingRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, BoardingRepository boardingRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.boardingRepository = boardingRepository;
    }

    @Transactional
    public ReviewResponseDTO addOrUpdateReview(Long studentId, Long boardingId, ReviewRequestDTO request) {
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found."));
        Boarding boarding = boardingRepository.findById(boardingId)
                .orElseThrow(() -> new IllegalArgumentException("Boarding house not found."));

        Optional<Review> existingReviewOpt = reviewRepository.findByBoardingIdAndStudentId(boardingId, studentId);

        Review review;
        if (existingReviewOpt.isPresent()) {
            review = existingReviewOpt.get();
            review.setRating(request.getRating());
            review.setComment(request.getComment());
        } else {
            review = new Review();
            review.setStudent(student);
            review.setBoarding(boarding);
            review.setRating(request.getRating());
            review.setComment(request.getComment());
        }

        Review savedReview = reviewRepository.save(review);
        return new ReviewResponseDTO(savedReview);
    }

    @Transactional(readOnly = true)
    public BoardingReviewsDTO getBoardingReviews(Long boardingId) {
        List<Review> reviews = reviewRepository.findByBoardingIdOrderByCreatedAtDesc(boardingId);

        Double averageRating = 0.0;
        if (!reviews.isEmpty()) {
            double sum = reviews.stream().mapToInt(Review::getRating).sum();
            averageRating = sum / reviews.size();
        }

        List<ReviewResponseDTO> reviewDTOs = reviews.stream()
                .map(ReviewResponseDTO::new)
                .collect(Collectors.toList());

        // Round to 1 decimal place
        averageRating = Math.round(averageRating * 10.0) / 10.0;

        return new BoardingReviewsDTO(averageRating, reviews.size(), reviewDTOs);
    }

    @Transactional
    public void deleteReview(Long studentId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found."));

        if (!review.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("You can only delete your own reviews.");
        }

        reviewRepository.delete(review);
    }
}
