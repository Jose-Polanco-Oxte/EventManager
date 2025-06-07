package jpolanco.springbootapp.event.infrastructure.adapters.output.repository;

import jpolanco.springbootapp.event.infrastructure.adapters.output.persistence.StaffRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStaffRoleRepository extends JpaRepository<StaffRoleEntity, String> {
}
