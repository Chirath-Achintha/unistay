package com.unistay.service;

import com.unistay.dto.BoardingRequestDTO;
import com.unistay.dto.BoardingResponseDTO;
import com.unistay.entity.Boarding;
import com.unistay.entity.BoardingImage;
import com.unistay.entity.User;
import com.unistay.repository.BoardingRepository;
import com.unistay.repository.ReviewRepository;
import com.unistay.repository.UserRepository;
import com.unistay.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardingService {

    private final BoardingRepository boardingRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final VisitRepository visitRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public BoardingService(BoardingRepository boardingRepository,
                           UserRepository userRepository,
                           CloudinaryService cloudinaryService,
                           VisitRepository visitRepository,
                           ReviewRepository reviewRepository) {
        this.boardingRepository = boardingRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.visitRepository = visitRepository;
        this.reviewRepository = reviewRepository;
    }

    // Saves a boarding place after checking that the owner is valid and uploading images.
    @Transactional
    public BoardingResponseDTO createBoarding(BoardingRequestDTO dto, MultipartFile[] images) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        if (!owner.getRole().name().equals("OWNER")) {
            throw new IllegalArgumentException("User is not an OWNER");
        }

        Boarding boarding = mapToEntity(dto, new Boarding());
        boarding.setOwner(owner);

        handleImageUploads(boarding, images);

        Boarding savedBoarding = boardingRepository.save(boarding);
        return enrichWithRating(new BoardingResponseDTO(savedBoarding));
    }

    @Transactional(readOnly = true)
    public List<BoardingResponseDTO> getBoardingsByOwner(Long ownerId) {
        return boardingRepository.findByOwnerId(ownerId).stream()
                .map(b -> enrichWithRating(new BoardingResponseDTO(b)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BoardingResponseDTO> getAllBoardings() {
        return boardingRepository.findAll().stream()
                .map(b -> enrichWithRating(new BoardingResponseDTO(b)))
                .collect(Collectors.toList());
    }

    // Filters the list of boardings according to the student's chosen city, budget, and room needs.
    @Transactional(readOnly = true)
    public List<BoardingResponseDTO> searchBoardings(String city, String university, String roomType,
                                                     Double minPrice, Double maxPrice,
                                                     Double minDistance, Double maxDistance) {
        if (university != null && (university.trim().isEmpty() || "Any".equalsIgnoreCase(university) || "Any University".equalsIgnoreCase(university))) {
            university = null;
        } else if (university != null) {
            university = university.trim();
        }

        if (roomType != null && (roomType.trim().isEmpty() || "Any".equalsIgnoreCase(roomType) || "Any Room Type".equalsIgnoreCase(roomType))) {
            roomType = null;
        } else if (roomType != null) {
            roomType = roomType.trim();
        }

        if (city != null && city.trim().isEmpty()) {
            city = null;
        } else if (city != null) {
            city = city.trim();
        }

        // If no filter parameters are applied, return all available boardings
        if (city == null && university == null && roomType == null &&
            minPrice == null && maxPrice == null && minDistance == null && maxDistance == null) {
            return getAllBoardings();
        }

        return boardingRepository.searchBoardings(city, university, roomType, minPrice, maxPrice, minDistance, maxDistance)
                .stream()
                .map(b -> enrichWithRating(new BoardingResponseDTO(b)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardingResponseDTO getBoardingById(Long id) {
        Boarding boarding = boardingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Boarding not found"));
        return enrichWithRating(new BoardingResponseDTO(boarding));
    }

    @Transactional
    public BoardingResponseDTO updateBoarding(Long id, BoardingRequestDTO dto, MultipartFile[] images) {
        Boarding boarding = boardingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Boarding not found"));

        if (!boarding.getOwner().getId().equals(dto.getOwnerId())) {
            throw new IllegalArgumentException("You do not have permission to modify this boarding");
        }

        boarding = mapToEntity(dto, boarding);

        if (images != null && images.length > 0) {
            handleImageUploads(boarding, images);
        }

        Boarding updatedBoarding = boardingRepository.save(boarding);
        return enrichWithRating(new BoardingResponseDTO(updatedBoarding));
    }

    // Removes a boarding listing only when the owner is allowed to delete it.
    // Visits and reviews linked to this boarding are deleted first to satisfy FK constraints.
    @Transactional
    public void deleteBoarding(Long id, Long ownerId) {
        Boarding boarding = boardingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Boarding not found"));

        if (!boarding.getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("You do not have permission to delete this boarding");
        }

        // Remove dependent records before deleting the boarding
        visitRepository.deleteByBoardingId(id);
        reviewRepository.deleteByBoardingId(id);

        boardingRepository.delete(boarding);
    }

    private Boarding mapToEntity(BoardingRequestDTO dto, Boarding boarding) {
        boarding.setName(dto.getName());
        boarding.setDescription(dto.getDescription());
        boarding.setAddress(dto.getAddress());
        boarding.setLocation(dto.getLocation());
        boarding.setGoogleMapsLink(dto.getGoogleMapsLink());
        boarding.setUniversity(dto.getUniversity());
        boarding.setPricePerMonth(dto.getPricePerMonth());
        boarding.setTotalRooms(dto.getTotalRooms());
        boarding.setAvailableRooms(dto.getAvailableRooms());
        boarding.setRoomType(dto.getRoomType());
        boarding.setStudentsPerRoom(dto.getStudentsPerRoom());
        boarding.setSuitableGender(dto.getSuitableGender());

        boarding.setHasBeds(dto.getHasBeds());
        boarding.setHasHotWater(dto.getHasHotWater());
        boarding.setHasKitchen(dto.getHasKitchen());
        boarding.setHasLaundry(dto.getHasLaundry());
        boarding.setHasAc(dto.getHasAc());
        boarding.setHasAttachedBathroom(dto.getHasAttachedBathroom());
        boarding.setHasCctv(dto.getHasCctv());
        boarding.setHasParking(dto.getHasParking());
        boarding.setHasWifi(dto.getHasWifi());
        boarding.setHasMainRoadAccess(dto.getHasMainRoadAccess());
        boarding.setDistanceFromUniversity(dto.getDistanceFromUniversity());
        boarding.setContactNumber(dto.getContactNumber());
        return boarding;
    }

    /**
     * Uploads each image to Cloudinary and attaches the returned secure URL to the boarding.
     */
    private void handleImageUploads(Boarding boarding, MultipartFile[] images) {
        if (images == null || images.length == 0) return;

        boolean firstImage = boarding.getImages().isEmpty();

        for (MultipartFile file : images) {
            if (file == null || file.isEmpty()) continue;

            String secureUrl = cloudinaryService.uploadImage(file);

            BoardingImage bImage = new BoardingImage(secureUrl, firstImage);
            boarding.addImage(bImage);
            firstImage = false; // only the first image is primary
        }
    }

    /**
     * Populates averageRating and reviewCount on a BoardingResponseDTO from the database.
     */
    private BoardingResponseDTO enrichWithRating(BoardingResponseDTO dto) {
        Double avg = reviewRepository.findAverageRatingByBoardingId(dto.getId());
        long count = reviewRepository.countByBoardingId(dto.getId());
        if (avg != null) {
            avg = Math.round(avg * 10.0) / 10.0;
        }
        dto.setAverageRating(avg != null ? avg : 0.0);
        dto.setReviewCount((int) count);
        return dto;
    }
}
