package com.unistay.repository;

import com.unistay.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<Visit> findByBoardingOwnerIdOrderByCreatedAtDesc(Long ownerId);

    /** Delete all visit records linked to a given boarding (used before boarding deletion). */
    void deleteByBoardingId(Long boardingId);

    /** Lookup a visit by id and student id (ownership check). */
    Optional<Visit> findByIdAndStudentId(Long id, Long studentId);
}
