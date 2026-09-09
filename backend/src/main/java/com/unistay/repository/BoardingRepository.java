package com.unistay.repository;

import com.unistay.entity.Boarding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardingRepository extends JpaRepository<Boarding, Long> {
    List<Boarding> findByOwnerId(Long ownerId);

    @org.springframework.data.jpa.repository.Query("SELECT b FROM Boarding b WHERE " +
            "(:city IS NULL OR LOWER(b.location) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:university IS NULL OR b.university = :university) AND " +
            "(:roomType IS NULL OR b.roomType = :roomType) AND " +
            "(:minPrice IS NULL OR b.pricePerMonth >= :minPrice) AND " +
            "(:maxPrice IS NULL OR b.pricePerMonth <= :maxPrice) AND " +
            "(:minDistance IS NULL OR b.distanceFromUniversity >= :minDistance) AND " +
            "(:maxDistance IS NULL OR b.distanceFromUniversity <= :maxDistance)")
    List<Boarding> searchBoardings(@org.springframework.data.repository.query.Param("city") String city,
                                   @org.springframework.data.repository.query.Param("university") String university,
                                   @org.springframework.data.repository.query.Param("roomType") String roomType,
                                   @org.springframework.data.repository.query.Param("minPrice") Double minPrice,
                                   @org.springframework.data.repository.query.Param("maxPrice") Double maxPrice,
                                   @org.springframework.data.repository.query.Param("minDistance") Double minDistance,
                                   @org.springframework.data.repository.query.Param("maxDistance") Double maxDistance);
}
