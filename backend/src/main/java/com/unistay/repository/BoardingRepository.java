package com.unistay.repository;

import com.unistay.entity.Boarding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardingRepository extends JpaRepository<Boarding, Long> {
    List<Boarding> findByOwnerId(Long ownerId);
}
