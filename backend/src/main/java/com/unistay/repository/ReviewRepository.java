package com.unistay.repository;

import com.unistay.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBoardingIdOrderByCreatedAtDesc(Long boardingId);
    Optional<Review> findByBoardingIdAndStudentId(Long boardingId, Long studentId);

    /** Delete all reviews linked to a given boarding (used before boarding deletion). */
    void deleteByBoardingId(Long boardingId);

    /** Returns the average rating for a given boarding (null if no reviews). */
    @org.springframework.data.jpa.repository.Query("SELECT AVG(r.rating) FROM Review r WHERE r.boarding.id = :boardingId")
    Double findAverageRatingByBoardingId(@org.springframework.data.repository.query.Param("boardingId") Long boardingId);

    /** Returns the number of reviews for a given boarding. */
    long countByBoardingId(Long boardingId);
}
