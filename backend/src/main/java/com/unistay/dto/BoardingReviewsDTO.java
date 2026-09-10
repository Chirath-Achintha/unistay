package com.unistay.dto;

import java.util.List;

public class BoardingReviewsDTO {
    private Double averageRating;
    private Integer totalReviews;
    private List<ReviewResponseDTO> reviews;

    public BoardingReviewsDTO() {}

    public BoardingReviewsDTO(Double averageRating, Integer totalReviews, List<ReviewResponseDTO> reviews) {
        this.averageRating = averageRating;
        this.totalReviews = totalReviews;
        this.reviews = reviews;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public List<ReviewResponseDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponseDTO> reviews) {
        this.reviews = reviews;
    }
}
