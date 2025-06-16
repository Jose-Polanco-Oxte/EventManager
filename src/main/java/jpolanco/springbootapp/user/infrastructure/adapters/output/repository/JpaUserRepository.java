package jpolanco.springbootapp.user.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String username);

    // Custom method to find users by last name, first name, or both
    @Query("SELECT u FROM UserEntity u WHERE " +
            "LOWER (CONCAT(u.firstName, ' ', u.lastName)) " +
            "LIKE LOWER(CONCAT('%', :name, '%')) ")
    List<UserEntity> searchByName(String name, Pageable pageable);

    // Custom method to find users by email
    @Query("SELECT u FROM UserEntity u WHERE " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    List<UserEntity> searchByEmail(String email, Pageable pageable);

    // Custom method to find users by cursor ID and created at date
    @Query("""
            SELECT u FROM UserEntity u
            WHERE u.id > :eventId AND u.createdAt >= :date
            ORDER BY u.id ASC, u.createdAt ASC
    """)
    Slice<UserEntity> findAllByCursorIdAndCreatedAt(UUID eventId, Instant date, Pageable pageable);
}