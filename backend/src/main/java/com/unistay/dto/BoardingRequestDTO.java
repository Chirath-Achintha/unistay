package com.unistay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class BoardingRequestDTO {

    @NotNull(message = "Owner ID is required")
    private Long ownerId;

    @NotBlank(message = "Boarding name is required")
    @Size(min = 3, max = 150)
    private String name;

    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Location is required")
    private String location;

    private String googleMapsLink;

    @NotNull(message = "Price per month is required")
    @PositiveOrZero
    private Double pricePerMonth;

    @NotNull
    @PositiveOrZero
    private Integer totalRooms;

    @NotNull
    @PositiveOrZero
    private Integer availableRooms;

    @NotBlank
    private String roomType;

    @NotNull
    @PositiveOrZero
    private Integer studentsPerRoom;

    @NotBlank
    private String suitableGender;

    private Boolean hasBeds = false;
    private Boolean hasHotWater = false;
    private Boolean hasKitchen = false;
    private Boolean hasLaundry = false;
    private Boolean hasAc = false;
    private Boolean hasAttachedBathroom = false;
    private Boolean hasCctv = false;
    private Boolean hasParking = false;
    private Boolean hasWifi = false;
    private Boolean hasMainRoadAccess = false;

    private Double distanceFromUniversity;

    // Getters and Setters

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getGoogleMapsLink() { return googleMapsLink; }
    public void setGoogleMapsLink(String googleMapsLink) { this.googleMapsLink = googleMapsLink; }

    public Double getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(Double pricePerMonth) { this.pricePerMonth = pricePerMonth; }

    public Integer getTotalRooms() { return totalRooms; }
    public void setTotalRooms(Integer totalRooms) { this.totalRooms = totalRooms; }

    public Integer getAvailableRooms() { return availableRooms; }
    public void setAvailableRooms(Integer availableRooms) { this.availableRooms = availableRooms; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public Integer getStudentsPerRoom() { return studentsPerRoom; }
    public void setStudentsPerRoom(Integer studentsPerRoom) { this.studentsPerRoom = studentsPerRoom; }

    public String getSuitableGender() { return suitableGender; }
    public void setSuitableGender(String suitableGender) { this.suitableGender = suitableGender; }

    public Boolean getHasBeds() { return hasBeds; }
    public void setHasBeds(Boolean hasBeds) { this.hasBeds = hasBeds; }

    public Boolean getHasHotWater() { return hasHotWater; }
    public void setHasHotWater(Boolean hasHotWater) { this.hasHotWater = hasHotWater; }

    public Boolean getHasKitchen() { return hasKitchen; }
    public void setHasKitchen(Boolean hasKitchen) { this.hasKitchen = hasKitchen; }

    public Boolean getHasLaundry() { return hasLaundry; }
    public void setHasLaundry(Boolean hasLaundry) { this.hasLaundry = hasLaundry; }

    public Boolean getHasAc() { return hasAc; }
    public void setHasAc(Boolean hasAc) { this.hasAc = hasAc; }

    public Boolean getHasAttachedBathroom() { return hasAttachedBathroom; }
    public void setHasAttachedBathroom(Boolean hasAttachedBathroom) { this.hasAttachedBathroom = hasAttachedBathroom; }

    public Boolean getHasCctv() { return hasCctv; }
    public void setHasCctv(Boolean hasCctv) { this.hasCctv = hasCctv; }

    public Boolean getHasParking() { return hasParking; }
    public void setHasParking(Boolean hasParking) { this.hasParking = hasParking; }

    public Boolean getHasWifi() { return hasWifi; }
    public void setHasWifi(Boolean hasWifi) { this.hasWifi = hasWifi; }

    public Boolean getHasMainRoadAccess() { return hasMainRoadAccess; }
    public void setHasMainRoadAccess(Boolean hasMainRoadAccess) { this.hasMainRoadAccess = hasMainRoadAccess; }

    public Double getDistanceFromUniversity() { return distanceFromUniversity; }
    public void setDistanceFromUniversity(Double distanceFromUniversity) { this.distanceFromUniversity = distanceFromUniversity; }
}
