package com.unistay.dto;

import com.unistay.entity.VisitStatus;

public class VisitStatusUpdateDTO {
    private VisitStatus status;
    private String proposedDate; // ISO format: "YYYY-MM-DD"

    public VisitStatusUpdateDTO() {}

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }

    public String getProposedDate() {
        return proposedDate;
    }

    public void setProposedDate(String proposedDate) {
        this.proposedDate = proposedDate;
    }
}
