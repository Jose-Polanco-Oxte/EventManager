package jpolanco.springbootapp.user.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String username);

    Optional<UserEntity> findByUuid(UUID uuid);

    // Custom method to find users by last name, first name, or both
    @Query("SELECT u FROM UserEntity u WHERE " +
            "u.fullNameLower " +
            "LIKE LOWER (CONCAT('%', :query, '%')) ")
    List<UserEntity> searchByName(@Param("query") String name, Pageable pageable);

    // Custom method to find users by email
    @Query("SELECT u FROM UserEntity u WHERE " +
            "u.emailLower LIKE LOWER (CONCAT('%', :query, '%'))")
    List<UserEntity> searchByEmail(@Param("query") String email, Pageable pageable);

    @Query("""
            SELECT u FROM UserEntity u
            WHERE u.id > :id
    """)
    Slice<UserEntity> findByIdGreaterThan(@Param("id") Long userId, Pageable pageable);
}