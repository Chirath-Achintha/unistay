package com.unistay.entity;

import jakarta.persistence.*;

/**
 * Entity representing an image attached to a Boarding.
 */
@Entity
@Table(name = "boarding_images")
public class BoardingImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boarding_id", nullable = false)
    private Boarding boarding;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    public BoardingImage() {
    }

    public BoardingImage(String imageUrl, Boolean isPrimary) {
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boarding getBoarding() {
        return boarding;
    }

    public void setBoarding(Boarding boarding) {
        this.boarding = boarding;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean primary) {
        isPrimary = primary;
    }
}
