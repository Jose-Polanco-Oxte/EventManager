package jpolanco.springbootapp.shared.infrastructure.auditory.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuditoryLogRepository extends JpaRepository<AuditoryLog, Long> {
}
