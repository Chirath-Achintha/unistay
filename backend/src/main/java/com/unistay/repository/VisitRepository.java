package com.unistay.repository;

import com.unistay.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<Visit> findByBoardingOwnerIdOrderByCreatedAtDesc(Long ownerId);
}
