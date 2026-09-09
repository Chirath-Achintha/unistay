package com.unistay.repository;

import com.unistay.entity.BoardingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardingImageRepository extends JpaRepository<BoardingImage, Long> {
}
