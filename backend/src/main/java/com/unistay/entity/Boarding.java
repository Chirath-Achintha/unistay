package com.unistay.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Boarding listing created by an Owner.
 */
@Entity
@Table(name = "boardings")
public class Boarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String location;

    @Column(name = "google_maps_link")
    private String googleMapsLink;

    @Column(name = "university")
    private String university;

    @Column(name = "price_per_month", nullable = false)
    private Double pricePerMonth;

    @Column(name = "total_rooms", nullable = false)
    private Integer totalRooms;

    @Column(name = "available_rooms", nullable = false)
    private Integer availableRooms;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @Column(name = "students_per_room", nullable = false)
    private Integer studentsPerRoom;

    @Column(name = "suitable_gender", nullable = false)
    private String suitableGender;

    // Facilities
    private Boolean hasBeds = false;
    private Boolean hasHotWater = false;
    private Boolean hasKitchen = false;
    private Boolean hasLaundry = false;
    private Boolean hasAc = false;
    private Boolean hasAttachedBathroom = false;
    private Boolean hasCctv = false;
    private Boolean hasParking = false;
    private Boolean hasWifi = false;

    // Other
    @Column(name = "has_main_road_access")
    private Boolean hasMainRoadAccess = false;

    @Column(name = "distance_from_university")
    private Double distanceFromUniversity; // in km

    @OneToMany(mappedBy = "boarding", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardingImage> images = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Boarding() {
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGoogleMapsLink() {
        return googleMapsLink;
    }

    public void setGoogleMapsLink(String googleMapsLink) {
        this.googleMapsLink = googleMapsLink;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Double getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(Double pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public Integer getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(Integer totalRooms) {
        this.totalRooms = totalRooms;
    }

    public Integer getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(Integer availableRooms) {
        this.availableRooms = availableRooms;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getStudentsPerRoom() {
        return studentsPerRoom;
    }

    public void setStudentsPerRoom(Integer studentsPerRoom) {
        this.studentsPerRoom = studentsPerRoom;
    }

    public String getSuitableGender() {
        return suitableGender;
    }

    public void setSuitableGender(String suitableGender) {
        this.suitableGender = suitableGender;
    }

    public Boolean getHasBeds() {
        return hasBeds;
    }

    public void setHasBeds(Boolean hasBeds) {
        this.hasBeds = hasBeds == null ? false : hasBeds;
    }

    public Boolean getHasHotWater() {
        return hasHotWater;
    }

    public void setHasHotWater(Boolean hasHotWater) {
        this.hasHotWater = hasHotWater == null ? false : hasHotWater;
    }

    public Boolean getHasKitchen() {
        return hasKitchen;
    }

    public void setHasKitchen(Boolean hasKitchen) {
        this.hasKitchen = hasKitchen == null ? false : hasKitchen;
    }

    public Boolean getHasLaundry() {
        return hasLaundry;
    }

    public void setHasLaundry(Boolean hasLaundry) {
        this.hasLaundry = hasLaundry == null ? false : hasLaundry;
    }

    public Boolean getHasAc() {
        return hasAc;
    }

    public void setHasAc(Boolean hasAc) {
        this.hasAc = hasAc == null ? false : hasAc;
    }

    public Boolean getHasAttachedBathroom() {
        return hasAttachedBathroom;
    }

    public void setHasAttachedBathroom(Boolean hasAttachedBathroom) {
        this.hasAttachedBathroom = hasAttachedBathroom == null ? false : hasAttachedBathroom;
    }

    public Boolean getHasCctv() {
        return hasCctv;
    }

    public void setHasCctv(Boolean hasCctv) {
        this.hasCctv = hasCctv == null ? false : hasCctv;
    }

    public Boolean getHasParking() {
        return hasParking;
    }

    public void setHasParking(Boolean hasParking) {
        this.hasParking = hasParking == null ? false : hasParking;
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi == null ? false : hasWifi;
    }

    public Boolean getHasMainRoadAccess() {
        return hasMainRoadAccess;
    }

    public void setHasMainRoadAccess(Boolean hasMainRoadAccess) {
        this.hasMainRoadAccess = hasMainRoadAccess == null ? false : hasMainRoadAccess;
    }

    public Double getDistanceFromUniversity() {
        return distanceFromUniversity;
    }

    public void setDistanceFromUniversity(Double distanceFromUniversity) {
        this.distanceFromUniversity = distanceFromUniversity;
    }

    public List<BoardingImage> getImages() {
        return images;
    }

    public void setImages(List<BoardingImage> images) {
        this.images = images;
    }

    public void addImage(BoardingImage image) {
        images.add(image);
        image.setBoarding(this);
    }

    public void removeImage(BoardingImage image) {
        images.remove(image);
        image.setBoarding(null);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
