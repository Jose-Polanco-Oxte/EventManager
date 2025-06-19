package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaStaffRepository extends JpaRepository<StaffEntity, UUID> {
    Optional<StaffEntity> findByUserIdAndEventId(UUID userId, UUID eventId);
    List<StaffEntity> findAllByUserId(UUID userId);
}