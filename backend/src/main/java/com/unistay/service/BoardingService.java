package com.unistay.service;

import com.unistay.dto.BoardingRequestDTO;
import com.unistay.dto.BoardingResponseDTO;
import com.unistay.entity.Boarding;
import com.unistay.entity.BoardingImage;
import com.unistay.entity.User;
import com.unistay.repository.BoardingRepository;
import com.unistay.repository.UserRepository;
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

    @Autowired
    public BoardingService(BoardingRepository boardingRepository,
                           UserRepository userRepository,
                           CloudinaryService cloudinaryService) {
        this.boardingRepository = boardingRepository;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

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
        return new BoardingResponseDTO(savedBoarding);
    }

    @Transactional(readOnly = true)
    public List<BoardingResponseDTO> getBoardingsByOwner(Long ownerId) {
        return boardingRepository.findByOwnerId(ownerId).stream()
                .map(BoardingResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardingResponseDTO getBoardingById(Long id) {
        Boarding boarding = boardingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Boarding not found"));
        return new BoardingResponseDTO(boarding);
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
        return new BoardingResponseDTO(updatedBoarding);
    }

    @Transactional
    public void deleteBoarding(Long id, Long ownerId) {
        Boarding boarding = boardingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Boarding not found"));

        if (!boarding.getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("You do not have permission to delete this boarding");
        }

        boardingRepository.delete(boarding);
    }

    private Boarding mapToEntity(BoardingRequestDTO dto, Boarding boarding) {
        boarding.setName(dto.getName());
        boarding.setDescription(dto.getDescription());
        boarding.setAddress(dto.getAddress());
        boarding.setLocation(dto.getLocation());
        boarding.setGoogleMapsLink(dto.getGoogleMapsLink());
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
}
