package com.unistay.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a Visit Request from a Student to a Boarding Owner.
 */
@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boarding_id", nullable = false)
    private Boarding boarding;

    @Column(name = "requested_date", nullable = false)
    private LocalDate requestedDate;

    @Column(name = "proposed_date")
    private LocalDate proposedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VisitStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Visit() {
        this.createdAt = LocalDateTime.now();
        this.status = VisitStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = VisitStatus.PENDING;
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Boarding getBoarding() {
        return boarding;
    }

    public void setBoarding(Boarding boarding) {
        this.boarding = boarding;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalDate getProposedDate() {
        return proposedDate;
    }

    public void setProposedDate(LocalDate proposedDate) {
        this.proposedDate = proposedDate;
    }

    public VisitStatus getStatus() {
        return status;
    }

    public void setStatus(VisitStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
