package com.unistay.repository;

import com.unistay.entity.User;
import com.unistay.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByRole(UserRole role);

    /**
     * Find all students whose name OR email contains the given search term (case-insensitive).
     */
    @Query("SELECT u FROM User u WHERE u.role = 'STUDENT' AND " +
           "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<User> searchStudents(@Param("term") String term);
}
