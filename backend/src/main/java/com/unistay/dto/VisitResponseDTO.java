package com.unistay.dto;

import com.unistay.entity.Visit;
import com.unistay.entity.VisitStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VisitResponseDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private String studentPhone;
    private Long boardingId;
    private String boardingName;
    private LocalDate requestedDate;
    private LocalDate proposedDate;
    private VisitStatus status;
    private LocalDateTime createdAt;

    public VisitResponseDTO() {}

    public VisitResponseDTO(Visit visit) {
        this.id = visit.getId();
        this.studentId = visit.getStudent().getId();
        this.studentName = visit.getStudent().getFullName();
        this.studentEmail = visit.getStudent().getEmail();
        this.studentPhone = visit.getStudent().getPhone();
        this.boardingId = visit.getBoarding().getId();
        this.boardingName = visit.getBoarding().getName();
        this.requestedDate = visit.getRequestedDate();
        this.proposedDate = visit.getProposedDate();
        this.status = visit.getStatus();
        this.createdAt = visit.getCreatedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    
    public String getStudentPhone() { return studentPhone; }
    public void setStudentPhone(String studentPhone) { this.studentPhone = studentPhone; }
    
    public Long getBoardingId() { return boardingId; }
    public void setBoardingId(Long boardingId) { this.boardingId = boardingId; }
    
    public String getBoardingName() { return boardingName; }
    public void setBoardingName(String boardingName) { this.boardingName = boardingName; }
    
    public LocalDate getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDate requestedDate) { this.requestedDate = requestedDate; }
    
    public LocalDate getProposedDate() { return proposedDate; }
    public void setProposedDate(LocalDate proposedDate) { this.proposedDate = proposedDate; }
    
    public VisitStatus getStatus() { return status; }
    public void setStatus(VisitStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
