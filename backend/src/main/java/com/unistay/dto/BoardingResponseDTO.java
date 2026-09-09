package com.unistay.dto;

import com.unistay.entity.Boarding;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BoardingResponseDTO {

    private Long id;
    private Long ownerId;
    private String ownerName;
    private String name;
    private String description;
    private String address;
    private String location;
    private String googleMapsLink;
    private String university;
    private Double pricePerMonth;
    private Integer totalRooms;
    private Integer availableRooms;
    private String roomType;
    private Integer studentsPerRoom;
    private String suitableGender;
    private Boolean hasBeds;
    private Boolean hasHotWater;
    private Boolean hasKitchen;
    private Boolean hasLaundry;
    private Boolean hasAc;
    private Boolean hasAttachedBathroom;
    private Boolean hasCctv;
    private Boolean hasParking;
    private Boolean hasWifi;
    private Boolean hasMainRoadAccess;
    private Double distanceFromUniversity;
    private LocalDateTime createdAt;
    
    private List<String> imageUrls;

    public BoardingResponseDTO(Boarding boarding) {
        this.id = boarding.getId();
        this.ownerId = boarding.getOwner().getId();
        this.ownerName = boarding.getOwner().getFullName();
        this.name = boarding.getName();
        this.description = boarding.getDescription();
        this.address = boarding.getAddress();
        this.location = boarding.getLocation();
        this.googleMapsLink = boarding.getGoogleMapsLink();
        this.university = boarding.getUniversity();
        this.pricePerMonth = boarding.getPricePerMonth();
        this.totalRooms = boarding.getTotalRooms();
        this.availableRooms = boarding.getAvailableRooms();
        this.roomType = boarding.getRoomType();
        this.studentsPerRoom = boarding.getStudentsPerRoom();
        this.suitableGender = boarding.getSuitableGender();
        this.hasBeds = boarding.getHasBeds();
        this.hasHotWater = boarding.getHasHotWater();
        this.hasKitchen = boarding.getHasKitchen();
        this.hasLaundry = boarding.getHasLaundry();
        this.hasAc = boarding.getHasAc();
        this.hasAttachedBathroom = boarding.getHasAttachedBathroom();
        this.hasCctv = boarding.getHasCctv();
        this.hasParking = boarding.getHasParking();
        this.hasWifi = boarding.getHasWifi();
        this.hasMainRoadAccess = boarding.getHasMainRoadAccess();
        this.distanceFromUniversity = boarding.getDistanceFromUniversity();
        this.createdAt = boarding.getCreatedAt();

        if (boarding.getImages() != null) {
            this.imageUrls = boarding.getImages().stream()
                    .map(img -> img.getImageUrl())
                    .collect(Collectors.toList());
        }
    }

    // Getters

    public Long getId() { return id; }
    public Long getOwnerId() { return ownerId; }
    public String getOwnerName() { return ownerName; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getAddress() { return address; }
    public String getLocation() { return location; }
    public String getGoogleMapsLink() { return googleMapsLink; }
    public String getUniversity() { return university; }
    public Double getPricePerMonth() { return pricePerMonth; }
    public Integer getTotalRooms() { return totalRooms; }
    public Integer getAvailableRooms() { return availableRooms; }
    public String getRoomType() { return roomType; }
    public Integer getStudentsPerRoom() { return studentsPerRoom; }
    public String getSuitableGender() { return suitableGender; }
    public Boolean getHasBeds() { return hasBeds; }
    public Boolean getHasHotWater() { return hasHotWater; }
    public Boolean getHasKitchen() { return hasKitchen; }
    public Boolean getHasLaundry() { return hasLaundry; }
    public Boolean getHasAc() { return hasAc; }
    public Boolean getHasAttachedBathroom() { return hasAttachedBathroom; }
    public Boolean getHasCctv() { return hasCctv; }
    public Boolean getHasParking() { return hasParking; }
    public Boolean getHasWifi() { return hasWifi; }
    public Boolean getHasMainRoadAccess() { return hasMainRoadAccess; }
    public Double getDistanceFromUniversity() { return distanceFromUniversity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<String> getImageUrls() { return imageUrls; }
}
