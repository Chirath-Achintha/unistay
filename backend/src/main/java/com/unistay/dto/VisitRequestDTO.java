package com.unistay.dto;

public class VisitRequestDTO {
    private Long boardingId;
    private String requestedDate; // ISO format: "YYYY-MM-DD"

    public VisitRequestDTO() {}

    public Long getBoardingId() {
        return boardingId;
    }

    public void setBoardingId(Long boardingId) {
        this.boardingId = boardingId;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }
}
