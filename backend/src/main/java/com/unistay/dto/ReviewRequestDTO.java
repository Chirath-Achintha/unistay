package com.unistay.dto;

public class ReviewRequestDTO {
    private Integer rating;
    private String comment;

    public ReviewRequestDTO() {}

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
